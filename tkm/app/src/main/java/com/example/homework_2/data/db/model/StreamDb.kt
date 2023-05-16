package com.example.homework_2.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.homework_2.data.model.TopicItem

@Entity(tableName = "stream")
internal class StreamDb(

    @PrimaryKey
    val streamId: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "topics")
    var topics: List<TopicItem>,

    @ColumnInfo(name = "isSubscribed")
    var isSubscribed: Boolean = false
)
