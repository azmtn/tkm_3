package com.example.homework_2.presentation.elm.profile

import vivid.money.elmslie.core.ElmStoreCompat

internal class ProfileElmStoreFactory(
    private val actor: ProfileActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = ProfileState(),
            reducer = ProfileReducer(),
            actor = actor
        )
    }

    fun provide() = store

}
