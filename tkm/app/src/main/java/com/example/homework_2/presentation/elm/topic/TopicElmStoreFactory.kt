package com.example.homework_2.presentation.elm.topic

import vivid.money.elmslie.core.ElmStoreCompat

internal class TopicElmStoreFactory(
    private val actor: TopicActor
) {
    private val store by lazy {
        ElmStoreCompat(
            initialState = TopicState(),
            reducer = TopicReducer(),
            actor = actor
        )
    }
    fun provide() = store
}
