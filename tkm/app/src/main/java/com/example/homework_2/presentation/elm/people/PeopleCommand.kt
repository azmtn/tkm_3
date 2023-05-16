package com.example.homework_2.presentation.elm.people

internal sealed class PeopleCommand {

    object LoadPeopleList : PeopleCommand()

    object SearchPeople : PeopleCommand()

    data class SearchPeopleByQuery(val query: String) : PeopleCommand()
}
