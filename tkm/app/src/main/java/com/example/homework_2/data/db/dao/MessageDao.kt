package com.example.homework_2.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homework_2.data.db.model.MessageDb
import io.reactivex.Completable
import io.reactivex.Single

@Dao
internal interface MessageDao {

    @Query("SELECT * FROM message WHERE topic_name == :topic")
    fun getMessageByName(topic: String): Single<List<MessageDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMessage(messages: List<MessageDb>): Single<List<Long>>

    @Query("DELETE FROM message WHERE topic_name == :topic")
    fun deleteMessage(topic: String): Completable

    @Query("DELETE FROM message WHERE id == :messageId")
    fun deleteMessageById(messageId: Long): Completable
}
