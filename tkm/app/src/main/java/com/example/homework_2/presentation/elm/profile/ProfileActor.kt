package com.example.homework_2.presentation.elm.profile

import com.example.homework_2.domain.interactor.ProfileInteractor
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

internal class ProfileActor(
    private val profileInteractor: ProfileInteractor
) : ActorCompat<ProfileCommand, ProfileEvent> {

    override fun execute(command: ProfileCommand): Observable<ProfileEvent> = when (command) {
        is ProfileCommand.LoadOwnProfile -> profileInteractor.loadOwnUser()
            .mapEvents(
                { user -> ProfileEvent.Internal.ProfileLoaded(listOf(user)) },
                { error -> ProfileEvent.Internal.ProfileLoadingError(error) }
            )

        is ProfileCommand.CreateUserFromBundle ->
            profileInteractor.createUserFromBundle(command.bundle)
                .mapEvents(
                    { user -> ProfileEvent.Internal.UserCreatedFromBundle(listOf(user)) },
                    { error -> ProfileEvent.Internal.ProfileLoadingError(error) }
                )
    }
}
