package com.example.homework_2.presentation.elm.profile

import android.os.Parcelable
import com.example.homework_2.domain.model.User
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
internal data class ProfileState(
    val items: @RawValue List<User> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = false
) : Parcelable
