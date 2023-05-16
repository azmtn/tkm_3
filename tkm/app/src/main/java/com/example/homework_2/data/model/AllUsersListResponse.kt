package com.example.homework_2.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class AllUsersListResponse (

    @SerialName("members")
    val members: List<UserItem>
)
