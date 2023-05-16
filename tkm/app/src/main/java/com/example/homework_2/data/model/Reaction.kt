package com.example.homework_2.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class Reaction(
    @SerialName("user_id")
    val userId: Long,

    @SerialName("emoji_name")
    val emojiName: String,

    @SerialName("emoji_code")
    val emojiCode: String
)
