package com.example.homework_2.presentation.elm.profile

internal sealed class ProfileEffect {

    data class ProfileLoadError(val error: Throwable) : ProfileEffect()

}
