package com.example.homework_2.di.component

import com.example.homework_2.di.ActivityScope
import com.example.homework_2.di.ApplicationComponent
import com.example.homework_2.di.module.ProfileModule
import com.example.homework_2.presentation.ProfileFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [ProfileModule::class],
    dependencies = [ApplicationComponent::class]
)
internal interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)

    @Component.Factory
    interface Factory {

        fun create(
            applicationComponent: ApplicationComponent
        ): ProfileComponent
    }
}
