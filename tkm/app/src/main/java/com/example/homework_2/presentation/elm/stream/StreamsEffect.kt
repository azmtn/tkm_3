package com.example.homework_2.presentation.elm.stream

import android.os.Bundle

internal sealed class StreamsEffect {

    data class StreamsListLoadError(val error: Throwable) : StreamsEffect()

    data class NavigateToTopic(val bundle: Bundle) : StreamsEffect()

}
