package com.example.homework_2.data.mapper

import com.example.homework_2.data.db.model.UserDb
import com.example.homework_2.domain.model.User

internal object UserMapper {

    internal fun List<User>.toModelList(): List<UserDb> =
        this.map { it.toModel() }

    private fun User.toModel(): UserDb {
        return UserDb(
            userId = this.userId,
            userFullName = this.fullName,
            email = this.email,
            avatarUrl = this.avatarUrl,
            presence = this.presence
        )
    }
}
