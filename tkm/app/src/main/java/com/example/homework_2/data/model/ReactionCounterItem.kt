package com.example.homework_2.data.model

internal data class ReactionCounterItem(
    val code: String,
    var count: Int,
    var selectedByCurrentUser: Boolean = false
)