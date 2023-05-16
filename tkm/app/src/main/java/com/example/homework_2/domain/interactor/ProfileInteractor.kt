package com.example.homework_2.domain.interactor

import android.os.Bundle
import com.example.homework_2.data.model.SELF_USER_ID
import com.example.homework_2.domain.repository.PeopleRepository
import com.example.homework_2.domain.model.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

internal class ProfileInteractor(
    private val peopleRepository: PeopleRepository
) {

    fun loadOwnUser(): Observable<User> {
        return Single.merge(
            peopleRepository.loadUserFromDb(SELF_USER_ID),
            peopleRepository.loadOwnUserFromApi()
        ).toObservable()
            .subscribeOn(Schedulers.io())

    }

    fun createUserFromBundle(bundle: Bundle): Single<User> {
        return peopleRepository.createUserFromBundle(bundle)
    }
}
