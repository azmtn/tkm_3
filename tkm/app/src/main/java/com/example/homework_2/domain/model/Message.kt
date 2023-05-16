package com.example.homework_2.domain.model

import com.example.homework_2.data.model.ReactionCounterItem

internal data class Message(
    val id: Long = 0,
    val userId: Long,
    val userFullName: String,
    val topicName: String,
    val avatarUrl: String?,
    val content: String,
    val emojis: MutableList<ReactionCounterItem>,
    val timestamp: Long
)
