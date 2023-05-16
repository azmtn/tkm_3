package com.example.homework_2.presentation.elm.stream

import vivid.money.elmslie.core.ElmStoreCompat

internal class StreamsElmStoreFactory(
    private val actor: StreamsActor
) {

    fun provide() = ElmStoreCompat(
        initialState = StreamsState(),
        reducer = StreamsReducer(),
        actor = actor
    )

}
