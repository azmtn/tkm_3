package com.example.homework_2.di.module

import android.content.Context
import androidx.room.Room
import com.example.homework_2.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    companion object {
        private const val DATABASE_NAME = "AppDB"
    }
}
