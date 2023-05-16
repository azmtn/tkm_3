package com.example.homework_2.di.component

import com.example.homework_2.di.ActivityScope
import com.example.homework_2.di.ApplicationComponent
import com.example.homework_2.di.module.PeopleModule
import com.example.homework_2.presentation.PeopleFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [PeopleModule::class],
    dependencies = [ApplicationComponent::class]
)
internal interface PeopleComponent {

    fun inject(peopleFragment: PeopleFragment)

    @Component.Factory
    interface Factory {

        fun create(
            applicationComponent: ApplicationComponent
        ): PeopleComponent
    }
}
