package com.example.homework_2.data.mapper

import com.example.homework_2.data.db.model.MessageDb
import com.example.homework_2.data.mapper.ReactionDbMapper.toModelList
import com.example.homework_2.data.model.ReactionCounterItem
import com.example.homework_2.domain.model.Message

internal object MessageDbMapper {

    internal fun List<MessageDb>.toModelList(): List<Message> =
        this.map { it.toModel() }

    internal fun MessageDb.toModel(): Message =
        Message(
            id = this.id,
            userId = this.userId,
            userFullName = this.userFullName,
            topicName = this.topicName,
            avatarUrl = this.avatarUrl,
            content = this.content,
            emojis = this.emojis.toModelList() as MutableList<ReactionCounterItem>,
            timestamp = this.timestamp
        )
}
