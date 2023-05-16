package com.example.homework_2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.homework_2.data.db.dao.MessageDao
import com.example.homework_2.data.db.dao.StreamDao
import com.example.homework_2.data.db.dao.UserDao
import com.example.homework_2.data.db.model.MessageDb
import com.example.homework_2.data.db.model.StreamDb
import com.example.homework_2.data.db.dao.SubscribedDao
import com.example.homework_2.data.db.model.SubscribedDb
import com.example.homework_2.data.db.model.UserDb

@Database(entities = [UserDb::class, MessageDb::class, StreamDb::class, SubscribedDb::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun messageDao(): MessageDao

    abstract fun streamDao(): StreamDao

    abstract fun subscribedDao(): SubscribedDao

}
