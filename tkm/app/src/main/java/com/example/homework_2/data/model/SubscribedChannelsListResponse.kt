package com.example.homework_2.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class SubscribedChannelsListResponse (

    @SerialName("subscriptions")
    val subscriptions: List<StreamItem>
)
