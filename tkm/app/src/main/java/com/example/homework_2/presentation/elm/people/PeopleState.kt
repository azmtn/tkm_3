package com.example.homework_2.presentation.elm.people

import android.os.Parcelable
import com.example.homework_2.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class PeopleState(
    val items: List<User> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = false
) : Parcelable
