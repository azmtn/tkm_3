package com.example.homework_2

import android.app.Application
import com.example.homework_2.di.ApplicationComponent
import com.example.homework_2.di.DaggerApplicationComponent

class App : Application() {

    internal lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.factory().create(
            applicationContext = this
        )
    }
}