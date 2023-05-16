package com.example.homework_2.data.mapper

import com.example.homework_2.data.model.TopicItem
import com.example.homework_2.domain.model.Topic

internal object TopicItemMapper {

    internal fun List<TopicItem>.toModelList(): List<Topic> =
        this.map { it.toModel() }

    private fun TopicItem.toModel(): Topic {
        return Topic(
            name = this.name
        )
    }
}
