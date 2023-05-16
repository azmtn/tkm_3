package com.example.homework_2.data.db.dao

import androidx.room.*
import com.example.homework_2.data.db.model.StreamDb
import io.reactivex.Single

@Dao
internal interface StreamDao {

    @Query("SELECT * FROM stream")
    fun getStream(): Single<List<StreamDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveStream(streams: List<StreamDb>): Single<List<Long>>
}
