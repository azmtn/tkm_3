package com.example.homework_2.domain.repository

import android.os.Bundle
import com.example.homework_2.domain.model.User
import io.reactivex.Single

internal interface PeopleRepository {
    fun loadUsersFromApi(): Single<List<User>>

    fun loadOwnUserFromApi(): Single<User>

    fun createUserFromBundle(bundle: Bundle): Single<User>

    fun loadUsersFromDb(): Single<List<User>>

    fun loadUserFromDb(userId: Long): Single<User>

    fun saveUsersToDb(users: List<User>)

}