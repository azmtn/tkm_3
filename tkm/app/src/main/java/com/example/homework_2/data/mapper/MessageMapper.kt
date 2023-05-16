package com.example.homework_2.data.mapper

import com.example.homework_2.data.db.model.MessageDb
import com.example.homework_2.data.mapper.ReactionMapper.toModelList
import com.example.homework_2.domain.model.Message

internal object MessageMapper {
    internal fun List<Message>.toModelList(): List<MessageDb> =
        this.map { it.toModel() }

    internal fun Message.toModel(): MessageDb =
        MessageDb(
            id = this.id,
            userId = this.userId,
            userFullName = this.userFullName,
            topicName = this.topicName,
            avatarUrl = this.avatarUrl,
            content = this.content,
            emojis = this.emojis.toModelList(),
            timestamp = this.timestamp
        )
}
