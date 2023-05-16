package com.example.homework_2.di.component

import com.example.homework_2.di.ActivityScope
import com.example.homework_2.di.ApplicationComponent
import com.example.homework_2.di.module.TopicModule
import com.example.homework_2.domain.interactor.TopicInteractor
import com.example.homework_2.domain.repository.TopicRepository
import com.example.homework_2.presentation.TopicActivity
import com.example.homework_2.presentation.elm.topic.TopicActor
import dagger.Component

@ActivityScope
@Component(
    modules = [TopicModule::class],
    dependencies = [ApplicationComponent::class]
)
internal interface TopicComponent {

    fun inject(topicActivity: TopicActivity)

    @Component.Factory
    interface Factory {

        fun create(
            applicationComponent: ApplicationComponent
        ): TopicComponent
    }

}
