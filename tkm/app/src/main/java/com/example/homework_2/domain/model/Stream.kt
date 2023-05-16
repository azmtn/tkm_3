package com.example.homework_2.domain.model

internal data class Stream(
    val streamId: Long = 0,
    val name: String,
    var topics: List<Topic>
)
