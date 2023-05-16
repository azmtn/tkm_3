package com.example.homework_2.presentation.elm.people

import vivid.money.elmslie.core.ElmStoreCompat

internal class PeopleElmStoreFactory(
    private val actor: PeopleActor
) {
    private val store by lazy {
        ElmStoreCompat(
            initialState = PeopleState(),
            reducer = PeopleReducer(),
            actor = actor
        )
    }
    fun provide() = store
}
