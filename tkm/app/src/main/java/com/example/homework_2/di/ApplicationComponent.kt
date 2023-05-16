package com.example.homework_2.di

import android.content.Context
import com.example.homework_2.data.db.AppDatabase
import com.example.homework_2.data.networking.ZulipApi
import com.example.homework_2.di.module.DatabaseModule
import com.example.homework_2.di.module.NetworkingModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NetworkingModule::class, DatabaseModule::class]
)
internal interface ApplicationComponent {
    fun getAppDatabase(): AppDatabase

    fun getApplicationContext(): Context

    fun getZulipJsonApi(): ZulipApi

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance
            applicationContext: Context
        ): ApplicationComponent
    }
}
