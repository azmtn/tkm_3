package com.example.homework_2.presentation.elm.topic

import android.os.Parcelable
import com.example.homework_2.data.networking.ZulipApi
import com.example.homework_2.domain.model.Message
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
internal data class TopicState(
    val items: @RawValue List<Message> = emptyList(),
    val error: Throwable? = null,
    val topicName: String = "",
    val isLoading: Boolean = false,
    val anchor: Long = ZulipApi.LAST_MESSAGES
) : Parcelable
