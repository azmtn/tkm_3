package com.example.homework_2.data.mapper

import com.example.homework_2.Utils
import com.example.homework_2.data.model.MessageItem
import com.example.homework_2.data.model.ReactionCounterItem
import com.example.homework_2.domain.model.Message

internal object MessageItemMapper {

    internal fun List<MessageItem>.toModelList(): List<Message> =
        this.map { it.toModel() }

    internal fun MessageItem.toModel(): Message =
        Message(
            id = this.id,
            userId = this.userId,
            userFullName = this.userFullName,
            topicName = this.topicName,
            avatarUrl = this.avatarUrl,
            content = this.content,
            emojis = Utils.getReactionForMessage(this.reactions) as MutableList<ReactionCounterItem>,
            timestamp = this.timestamp
        )
}
