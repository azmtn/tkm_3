package com.example.homework_2.data.db.dao

import androidx.room.*
import com.example.homework_2.data.db.model.SubscribedDb
import io.reactivex.Single

@Dao
internal interface SubscribedDao {

    @Query("SELECT * FROM subscribed")
    fun getSubscribed(): Single<List<SubscribedDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSubscribed(subscribedDb: List<SubscribedDb>): Single<List<Long>>
}
