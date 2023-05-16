package com.example.homework_2.eml

import com.example.homework_2.RxRule
import com.example.homework_2.data.StreamsRepositoryImpl
import com.example.homework_2.domain.interactor.ChannelsInteractor
import com.example.homework_2.domain.model.Stream
import com.example.homework_2.domain.model.Topic
import com.example.homework_2.presentation.elm.stream.StreamsActor
import com.example.homework_2.presentation.elm.stream.StreamsCommand
import com.example.homework_2.presentation.elm.stream.StreamsEvent
import com.example.homework_2.stub.AppDatabaseStub
import com.example.homework_2.stub.ZulipApiStub
import org.junit.Rule
import org.junit.Test

internal class StreamsActorTest {

    @get:Rule
    val rxRule = RxRule()

    @Test
    fun `loadStreamsList by default returns StreamsEvent observable`() {
        val zulipApiStub = ZulipApiStub.instance
        val repository = StreamsRepositoryImpl(zulipApiStub, AppDatabaseStub())
        val interactor = ChannelsInteractor(repository)
        val actor = StreamsActor(interactor)

        val testObserver = actor.execute(
            StreamsCommand.LoadStreamsList(true)
        ).test()

        testObserver.assertValueAt(
            0,
            StreamsEvent.Internal.StreamsListLoaded(
                listOf(
                    Stream(
                        streamId = 1L,
                        name = "first test stream",
                        topics = listOf(
                            Topic("first test topic")
                        )
                    ),
                    Stream(
                        streamId = 2L,
                        name = "second test stream",
                        topics = listOf(
                            Topic("second test topic"),
                            Topic("third test topic")
                        )
                    )
                )
            )
        )

        testObserver.assertValueAt(
            1,
            StreamsEvent.Internal.StreamsListLoaded(
                listOf(
                    Stream(
                        streamId = 1L,
                        name = "first test stream",
                        topics = listOf(
                            Topic("first test topic")
                        )
                    ),
                    Stream(
                        streamId = 2L,
                        name = "second test stream",
                        topics = listOf(
                            Topic("second test topic"),
                            Topic("third test topic")
                        )
                    ),
                    Stream(
                        streamId = 3L,
                        name = "third test stream",
                        topics = listOf()
                    )
                )
            )
        )
    }

}
