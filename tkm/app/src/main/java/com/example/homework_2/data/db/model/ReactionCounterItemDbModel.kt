package com.example.homework_2.data.db.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ReactionCounterItemDbModel(
    val code: String,
    val count: Int,
    var selectedByCurrentUser: Boolean = false
)
