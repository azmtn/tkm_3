package com.example.homework_2.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StreamItem(

    @SerialName("stream_id")
    val streamId: Long,

    @SerialName("name")
    val name: String,

    var topicItems: List<TopicItem> = listOf(),
)
