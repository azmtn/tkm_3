package com.example.homework_2.presentation.elm.stream

import com.example.homework_2.domain.interactor.ChannelsInteractor
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

internal class StreamsActor(
    private val channelsInteractor: ChannelsInteractor
) : ActorCompat<StreamsCommand, StreamsEvent> {

    override fun execute(command: StreamsCommand): Observable<StreamsEvent> = when (command) {
        is StreamsCommand.LoadStreamsList ->
            channelsInteractor.loadStreams(command.isSubscribedStreams)
                .mapEvents(
                    { streams -> StreamsEvent.Internal.StreamsListLoaded(items = streams) },
                    { error -> StreamsEvent.Internal.StreamsListLoadingError(error) }
                )

        is StreamsCommand.SubscribeOnSearchStreamsEvents ->
            channelsInteractor.subscribeOnSearchStreamsEvents()
                .mapEvents(
                    { streams -> StreamsEvent.Internal.StreamsWithSearchLoaded(items = streams) },
                    { error -> StreamsEvent.Internal.StreamsListLoadingError(error) }
                )

        is StreamsCommand.SearchStreamsByQuery -> {
            channelsInteractor.processSearchQuery(command.query)
            Observable.empty()
        }
    }
}
