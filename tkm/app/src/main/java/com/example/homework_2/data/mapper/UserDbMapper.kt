package com.example.homework_2.data.mapper

import com.example.homework_2.data.db.model.UserDb
import com.example.homework_2.domain.model.User

internal object UserDbMapper {

    fun List<UserDb>.toModelList(): List<User> =
        this.map { it.toModel() }

    fun UserDb.toModel(): User {
        return User(
            userId = this.userId,
            fullName = this.userFullName,
            email = this.email,
            avatarUrl = this.avatarUrl,
            presence = this.presence
        )
    }
}
