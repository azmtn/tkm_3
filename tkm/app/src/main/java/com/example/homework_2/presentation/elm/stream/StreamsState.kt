package com.example.homework_2.presentation.elm.stream

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import com.example.homework_2.domain.model.Stream

@Parcelize
internal data class StreamsState(
    val items: @RawValue List<Stream> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isTopicOpen: Boolean = false
) : Parcelable
