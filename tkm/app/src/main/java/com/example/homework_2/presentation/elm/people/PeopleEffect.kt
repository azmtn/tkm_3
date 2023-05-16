package com.example.homework_2.presentation.elm.people

import android.os.Bundle

internal sealed class PeopleEffect {

    data class PeopleListLoadError(val error: Throwable) : PeopleEffect()

    data class NavigateToProfile(val bundle: Bundle) : PeopleEffect()

}
