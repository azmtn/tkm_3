package com.example.homework_2.di.module

import com.example.homework_2.data.TopicRepositoryImpl
import com.example.homework_2.di.ActivityScope
import com.example.homework_2.domain.interactor.TopicInteractor
import com.example.homework_2.domain.repository.TopicRepository
import com.example.homework_2.presentation.elm.topic.TopicActor
import com.example.homework_2.presentation.elm.topic.TopicElmStoreFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [TopicModule.BindingModule::class])
internal class TopicModule {

    @Provides
    @ActivityScope
    fun provideChatInteractor(repository: TopicRepository): TopicInteractor =
        TopicInteractor(repository)

    @Provides
    @ActivityScope
    fun provideTopicActor(topicInteractor: TopicInteractor): TopicActor =
        TopicActor(topicInteractor)

    @Provides
    @ActivityScope
    fun provideTopictElmStoreFactory(topicActor: TopicActor): TopicElmStoreFactory =
        TopicElmStoreFactory(topicActor)

    @Module
    interface BindingModule {

        @Binds
        fun bindChatRepositoryImpl(impl: TopicRepositoryImpl): TopicRepository
    }

}
