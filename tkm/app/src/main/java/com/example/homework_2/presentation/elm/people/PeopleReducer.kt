package com.example.homework_2.presentation.elm.people

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

internal class PeopleReducer : DslReducer<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand>() {

    override fun Result.reduce(event: PeopleEvent): Any {
        return when (event) {
            is PeopleEvent.Internal.PeopleWithSearchLoaded -> {
                state {
                    copy(
                        items = event.items,
                        isLoading = false,
                        error = null
                    )
                }
            }
            is PeopleEvent.Ui.SearchPeopleByQuery -> {
                state {
                    copy(
                        isLoading = true,
                        error = null
                    )
                }
                commands { +PeopleCommand.SearchPeopleByQuery((event.query)) }
            }
            is PeopleEvent.Ui.LoadPeopleList -> {
                state {
                    copy(
                        isLoading = true,
                        error = null
                    )
                }
                commands { +PeopleCommand.LoadPeopleList }
            }
            is PeopleEvent.Ui.LoadProfile -> {
                state {
                    copy(
                        isLoading = false,
                        error = null
                    )
                }
                effects { +PeopleEffect.NavigateToProfile(event.bundle) }
            }
            is PeopleEvent.Internal.PeopleListLoaded -> {
                state {
                    copy(
                        items = event.items,
                        isLoading = false,
                        error = null
                    )
                }
            }
            is PeopleEvent.Internal.PeopleListLoadingError -> {
                state {
                    copy(
                        error = event.error,
                        isLoading = false
                    )
                }
                effects { +PeopleEffect.PeopleListLoadError(event.error) }
            }
        }
    }
}
