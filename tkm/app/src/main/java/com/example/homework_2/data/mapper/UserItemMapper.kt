package com.example.homework_2.data.mapper

import com.example.homework_2.data.model.UserItem
import com.example.homework_2.domain.model.User

internal object UserItemMapper {

    internal fun UserItem.toModel(): User {
        return User(
            userId = this.userId,
            fullName = this.fullName,
            email = this.email,
            avatarUrl = this.avatarUrl,
            presence = this.presence
        )
    }
}
