package com.example.homework_2.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homework_2.data.db.model.UserDb
import io.reactivex.Single

@Dao
internal interface UserDao {

    @Query("SELECT * FROM user WHERE userId == :userId")
    fun getUserById(userId: Long): Single<UserDb>

    @Query("SELECT * FROM user")
    fun getAllUser(): Single<List<UserDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(users: List<UserDb>): Single<List<Long>>
}
