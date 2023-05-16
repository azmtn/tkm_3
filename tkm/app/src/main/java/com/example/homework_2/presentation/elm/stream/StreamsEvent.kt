package com.example.homework_2.presentation.elm.stream

import android.os.Bundle
import com.example.homework_2.domain.model.Stream

internal sealed class StreamsEvent {

    sealed class Ui : StreamsEvent() {

        object LoadAllStreamsList : Ui()

        object LoadSubscribedStreamsList : Ui()

        data class LoadTopic(val bundle: Bundle) : Ui()

        object SubscribeOnSearchStreamsEvents : Ui()

        data class SearchStreamsByQuery(val query: String) : Ui()

    }

    sealed class Internal : StreamsEvent() {

        data class StreamsListLoaded(val items: List<Stream>) : Internal()

        data class StreamsWithSearchLoaded(val items: List<Stream>) : Internal()

        data class StreamsListLoadingError(val error: Throwable) : Internal()

    }

}
