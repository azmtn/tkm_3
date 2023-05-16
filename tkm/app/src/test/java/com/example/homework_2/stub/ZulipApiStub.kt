package com.example.homework_2.stub

import com.example.homework_2.data.model.StreamItem
import com.example.homework_2.data.model.SubscribedChannelsListResponse
import com.example.homework_2.data.model.TopicItem
import com.example.homework_2.data.model.TopicsListResponse
import com.example.homework_2.data.networking.ZulipApi
import io.reactivex.Single
import org.mockito.Mockito

internal object ZulipApiStub {

    var instance: ZulipApi = Mockito.mock(ZulipApi::class.java)

    init {
        mockGetSubscribedStreams()
        mockGetTopicsInStream()
    }

    private fun mockGetSubscribedStreams() {
        Mockito.`when`(instance.getSubscribedStreams()).thenReturn(
            Single.just(
                SubscribedChannelsListResponse(
                    listOf(
                        StreamItem(
                            streamId = 1L,
                            name = "stream_1",
                            topicItems = listOf()
                        ),
                        StreamItem(
                            streamId = 2L,
                            name = "stream_2",
                            topicItems = listOf()
                        ),
                        StreamItem(
                            streamId = 3L,
                            name = "stream_3",
                            topicItems = listOf()
                        )
                    )
                )
            )
        )
    }

    private fun mockGetTopicsInStream() {
        Mockito.`when`(instance.getTopicsInStream(1L)).thenReturn(
            Single.just(
                TopicsListResponse(
                    listOf(
                        TopicItem("topic_1")
                    )
                )
            )
        )

        Mockito.`when`(instance.getTopicsInStream(2L)).thenReturn(
            Single.just(
                TopicsListResponse(
                    listOf(
                        TopicItem("topic_2"),
                        TopicItem("topic_3")
                    )
                )
            )
        )

        Mockito.`when`(instance.getTopicsInStream(3L)).thenReturn(
            Single.just(
                TopicsListResponse(
                    listOf()
                )
            )
        )
    }
}
