package com.example.homework_2.data

import android.os.Bundle
import android.util.Log
import com.example.homework_2.data.db.AppDatabase
import com.example.homework_2.data.mapper.UserDbMapper.toModel
import com.example.homework_2.data.mapper.UserDbMapper.toModelList
import com.example.homework_2.data.mapper.UserItemMapper.toModel
import com.example.homework_2.data.mapper.UserMapper.toModelList
import com.example.homework_2.data.model.UserItem
import com.example.homework_2.data.model.UserPresenceResponse
import com.example.homework_2.data.networking.ZulipApi
import com.example.homework_2.domain.repository.PeopleRepository
import com.example.homework_2.domain.model.User
import com.example.homework_2.presentation.PeopleFragment
import com.example.homework_2.presentation.ProfileFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class PeopleRepositoryImpl @Inject constructor(
    private val getZulipApi: ZulipApi,
    private val db: AppDatabase
) :
    PeopleRepository {

    override fun loadUsersFromApi(): Single<List<User>> {
        return getZulipApi.getAllUsers()
            .flattenAsObservable { it.members }
            .flatMapSingle { getUserPresence(it) }
            .doOnError {
                Log.e("PeopleRepository", "Api users error", it)
            }
            .toList()
    }

    override fun loadOwnUserFromApi(): Single<User> {
        return getZulipApi.getOwnUser()
            .flatMap { user -> getUserPresence(user) }
            .doOnError {
                Log.e("PeopleRepository", "Db users error", it)
            }
    }

    override fun createUserFromBundle(bundle: Bundle): Single<User> {
        @Suppress("DEPRECATION")
        return Single.just(
            bundle.getParcelable(ProfileFragment.USER_KEY)
        )
    }

    private fun getUserPresence(user: UserItem): Single<User> {
        return getZulipApi.getUserPresence(userIdOrEmail = user.userId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                user.presence =
                    it.presence.aggregated?.status ?: PeopleFragment.NOT_FOUND_PRESENCE_KEY
            }
            .onErrorReturn {
                user.presence = PeopleFragment.NOT_FOUND_PRESENCE_KEY
                UserPresenceResponse()
            }
            .map { user.toModel() }
    }

    override fun loadUsersFromDb(): Single<List<User>> {
        return db.userDao().getAllUser()
            .map { it.toModelList() }
    }

    override fun loadUserFromDb(userId: Long): Single<User> {
        return db.userDao().getUserById(userId)
            .map { it.toModel() }
    }
    override fun saveUsersToDb(users: List<User>) {
        db.userDao().saveUser(users.toModelList())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn {
                emptyList()
            }.subscribe()
    }
}
