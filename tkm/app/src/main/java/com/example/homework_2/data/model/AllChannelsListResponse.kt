package com.example.homework_2.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AllChannelsListResponse (

    @SerialName("streams")
    val streams: List<StreamItem>
)
