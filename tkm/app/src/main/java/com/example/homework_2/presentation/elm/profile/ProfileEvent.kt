package com.example.homework_2.presentation.elm.profile

import android.os.Bundle
import com.example.homework_2.domain.model.User

internal sealed class ProfileEvent {

    sealed class Ui : ProfileEvent() {

        object InitEvent : Ui()

        object LoadOwnProfile: Ui()

        data class LoadProfile(val bundle: Bundle) : Ui()

    }

    sealed class Internal : ProfileEvent() {

        data class ProfileLoaded(val items: List<User>) : Internal()

        data class UserCreatedFromBundle(val items: List<User>) : Internal()

        data class ProfileLoadingError(val error: Throwable) : Internal()

    }
}
