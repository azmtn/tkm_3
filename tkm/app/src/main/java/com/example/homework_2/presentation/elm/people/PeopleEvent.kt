package com.example.homework_2.presentation.elm.people

import android.os.Bundle
import com.example.homework_2.domain.model.User

internal sealed class PeopleEvent {

    sealed class Ui : PeopleEvent() {

        object LoadPeopleList : Ui()

        data class LoadProfile(val bundle: Bundle) : Ui()

        data class SearchPeopleByQuery(val query: String) : Ui()
    }

    sealed class Internal : PeopleEvent() {

        data class PeopleListLoaded(val items: List<User>) : Internal()

        data class PeopleListLoadingError(val error: Throwable) : Internal()

        data class PeopleWithSearchLoaded(val items: List<User>) : Internal()
    }
}
