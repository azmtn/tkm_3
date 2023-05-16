package com.example.homework_2.di.module

import com.example.homework_2.di.ActivityScope
import com.example.homework_2.domain.repository.PeopleRepository
import com.example.homework_2.domain.interactor.ProfileInteractor
import com.example.homework_2.presentation.elm.profile.ProfileActor
import com.example.homework_2.presentation.elm.profile.ProfileElmStoreFactory
import dagger.Module
import dagger.Provides

@Module(includes = [PeopleModule.BindingModule::class])
internal class ProfileModule {

    @Provides
    @ActivityScope
    fun provideProfileInteractor(repository: PeopleRepository): ProfileInteractor =
        ProfileInteractor(repository)

    @Provides
    @ActivityScope
    fun provideProfileActor(profileInteractor: ProfileInteractor): ProfileActor =
        ProfileActor(profileInteractor)

    @Provides
    @ActivityScope
    fun provideProfileElmStoreFactory(profileActor: ProfileActor): ProfileElmStoreFactory =
        ProfileElmStoreFactory(profileActor)
}
