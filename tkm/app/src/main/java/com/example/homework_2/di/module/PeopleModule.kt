package com.example.homework_2.di.module

import com.example.homework_2.data.PeopleRepositoryImpl
import com.example.homework_2.di.ActivityScope
import com.example.homework_2.domain.interactor.PeopleInteractor
import com.example.homework_2.domain.repository.PeopleRepository
import com.example.homework_2.presentation.elm.people.PeopleActor
import com.example.homework_2.presentation.elm.people.PeopleElmStoreFactory
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module(includes = [PeopleModule.BindingModule::class])
internal class PeopleModule {

    @Provides
    @ActivityScope
    fun providePeopleInteractor(repository: PeopleRepository): PeopleInteractor =
        PeopleInteractor(repository)

    @Provides
    @ActivityScope
    fun providePeopleActor(peopleInteractor: PeopleInteractor): PeopleActor =
        PeopleActor(peopleInteractor)

    @Provides
    @ActivityScope
    fun providePeopleElmStoreFactory(peopleActor: PeopleActor): PeopleElmStoreFactory =
        PeopleElmStoreFactory(peopleActor)

    @Module
    interface BindingModule {

        @Binds
        fun bindPeopleRepositoryImpl(impl: PeopleRepositoryImpl): PeopleRepository
    }
}
