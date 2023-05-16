package com.example.homework_2.presentation.elm.profile

import android.os.Bundle

internal sealed class ProfileCommand {

    object LoadOwnProfile : ProfileCommand()

    data class CreateUserFromBundle(val bundle: Bundle) : ProfileCommand()

}
