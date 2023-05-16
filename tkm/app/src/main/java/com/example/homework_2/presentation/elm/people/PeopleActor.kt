package com.example.homework_2.presentation.elm.people

import com.example.homework_2.domain.interactor.PeopleInteractor
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

internal class PeopleActor(
    private val peopleInteractor: PeopleInteractor
) : ActorCompat<PeopleCommand, PeopleEvent> {

    override fun execute(command: PeopleCommand): Observable<PeopleEvent> = when (command) {
        is PeopleCommand.LoadPeopleList -> peopleInteractor.loadUsers()
            .mapEvents(
                { list -> PeopleEvent.Internal.PeopleListLoaded(list) },
                { error -> PeopleEvent.Internal.PeopleListLoadingError(error) }
            )

        is PeopleCommand.SearchPeople ->
            peopleInteractor.searchEvents()
                .mapEvents(
                    { people -> PeopleEvent.Internal.PeopleWithSearchLoaded(items = people) },
                    { error -> PeopleEvent.Internal.PeopleListLoadingError(error) }
                )

        is PeopleCommand.SearchPeopleByQuery -> {
            peopleInteractor.processSearchQuery(command.query)
            Observable.empty()
        }
    }
}
