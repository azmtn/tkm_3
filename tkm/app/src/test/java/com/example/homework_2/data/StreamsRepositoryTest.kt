package com.example.homework_2.data

import com.example.homework_2.RxRule
import com.example.homework_2.data.networking.ZulipApi
import com.example.homework_2.domain.model.Stream
import com.example.homework_2.domain.model.Topic
import com.example.homework_2.stub.AppDatabaseStub
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

internal class StreamsRepositoryTest {

    @get:Rule
    val rxRule = RxRule()

    @Test
    fun `loadStreamsFromDb by default streams list`() {
        val repository = StreamsRepositoryImpl(
            Mockito.mock(ZulipApi::class.java),
            AppDatabaseStub()
        )

        val testObserver = repository.loadStreamsFromDb(false).test()

        testObserver.assertValue(
            listOf(
                Stream(
                    streamId = 1L,
                    name = "stream_1",
                    topics = listOf(
                        Topic("topic_1"),
                        Topic("topic_4")
                    )
                ),
                Stream(
                    streamId = 2L,
                    name = "stream_2",
                    topics = listOf(
                        Topic("topic_2"),
                        Topic("topic_3")
                    )
                )
            )
        )
    }

}
