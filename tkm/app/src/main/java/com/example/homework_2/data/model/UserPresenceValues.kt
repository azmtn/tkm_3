package com.example.homework_2.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal  class UserPresenceValues (

    @SerialName("aggregated")
    val aggregated: UserPresence? = null
)
