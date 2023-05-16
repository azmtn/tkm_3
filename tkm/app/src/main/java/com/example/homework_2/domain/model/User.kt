package com.example.homework_2.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val userId: Long,
    val fullName: String? = "",
    val email: String? = "",
    val avatarUrl: String?,
    var presence: String? = "undefined"
) : Parcelable