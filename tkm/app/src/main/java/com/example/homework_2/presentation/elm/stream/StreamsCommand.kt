package com.example.homework_2.presentation.elm.stream

internal sealed class StreamsCommand {

    data class LoadStreamsList(val isSubscribedStreams: Boolean = false) : StreamsCommand()

    object SubscribeOnSearchStreamsEvents : StreamsCommand()

    data class SearchStreamsByQuery(val query: String) : StreamsCommand()

}
