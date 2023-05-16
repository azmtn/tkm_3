package com.example.homework_2.presentation.elm.stream

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

internal class StreamsReducer
    : DslReducer<StreamsEvent, StreamsState, StreamsEffect, StreamsCommand>() {

    override fun Result.reduce(event: StreamsEvent): Any {
        return when (event) {
            is StreamsEvent.Ui.LoadAllStreamsList -> {
                state {
                    copy(
                        isLoading = true,
                        error = null
                    )
                }
                commands { +StreamsCommand.LoadStreamsList() }
            }
            is StreamsEvent.Ui.LoadSubscribedStreamsList -> {
                state {
                    copy(
                        isLoading = true,
                        error = null
                    )
                }
                commands { +StreamsCommand.LoadStreamsList(isSubscribedStreams = true) }
            }
            is StreamsEvent.Ui.LoadTopic -> {
                state {
                    copy(
                        isLoading = false,
                        error = null
                    )
                }
                effects { +StreamsEffect.NavigateToTopic(event.bundle) }
            }
            is StreamsEvent.Ui.SubscribeOnSearchStreamsEvents -> {
                state {
                    copy(
                        isLoading = false,
                        error = null
                    )
                }
                commands { +StreamsCommand.SubscribeOnSearchStreamsEvents }
            }
            is StreamsEvent.Ui.SearchStreamsByQuery -> {
                state {
                    copy(
                        isLoading = true,
                        error = null
                    )
                }
                commands { +StreamsCommand.SearchStreamsByQuery((event.query)) }
            }

            is StreamsEvent.Internal.StreamsListLoaded -> {
                state {
                    copy(
                        items = event.items,
                        isLoading = false,
                        error = null
                    )
                }
            }
            is StreamsEvent.Internal.StreamsWithSearchLoaded -> {
                state {
                    copy(
                        items = event.items,
                        isLoading = false,
                        error = null
                    )
                }
            }
            is StreamsEvent.Internal.StreamsListLoadingError -> {
                state {
                    copy(
                        error = event.error,
                        isLoading = false
                    )
                }
                effects { +StreamsEffect.StreamsListLoadError(event.error) }
            }
        }
    }
}
