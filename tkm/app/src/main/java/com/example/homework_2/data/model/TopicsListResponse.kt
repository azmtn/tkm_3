package com.example.homework_2.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class TopicsListResponse (

    @SerialName("topics")
    val topicItems: List<TopicItem>
)
