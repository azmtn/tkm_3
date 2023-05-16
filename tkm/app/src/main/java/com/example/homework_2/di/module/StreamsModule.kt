package com.example.homework_2.di.module

import com.example.homework_2.data.StreamsRepositoryImpl
import com.example.homework_2.di.ActivityScope
import com.example.homework_2.domain.interactor.ChannelsInteractor
import com.example.homework_2.domain.repository.StreamRepository
import com.example.homework_2.presentation.elm.stream.StreamsActor
import com.example.homework_2.presentation.elm.stream.StreamsElmStoreFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [StreamsModule.BindingModule::class])
internal class StreamsModule {

    @Provides
    @ActivityScope
    fun provideChannelsInteractor(repository: StreamRepository): ChannelsInteractor =
        ChannelsInteractor(repository)

    @Provides
    @ActivityScope
    fun provideStreamsActor(channelsInteractor: ChannelsInteractor): StreamsActor =
        StreamsActor(channelsInteractor)

    @Provides
    @ActivityScope
    fun provideStreamsElmStoreFactory(streamsActor: StreamsActor): StreamsElmStoreFactory =
        StreamsElmStoreFactory(streamsActor)

    @Module
    interface BindingModule {

        @Binds
        fun bindStreamsRepositoryImpl(impl: StreamsRepositoryImpl): StreamRepository
    }
}
