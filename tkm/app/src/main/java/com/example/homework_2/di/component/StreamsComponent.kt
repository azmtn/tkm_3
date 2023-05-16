package com.example.homework_2.di.component

import com.example.homework_2.di.ActivityScope
import com.example.homework_2.di.ApplicationComponent
import com.example.homework_2.di.module.StreamsModule
import com.example.homework_2.presentation.ChannelsFragment
import com.example.homework_2.presentation.StreamsListFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [StreamsModule::class],
    dependencies = [ApplicationComponent::class]
)
internal interface StreamsComponent {

    fun inject(streamsListFragment: StreamsListFragment)

    fun inject(channelsFragment: ChannelsFragment)

    @Component.Factory
    interface Factory {

        fun create(
            applicationComponent: ApplicationComponent
        ): StreamsComponent
    }

}
