package com.example.homework_2.data.mapper

import com.example.homework_2.data.model.TopicItem
import com.example.homework_2.domain.model.Topic

internal object TopicMapper {

    internal fun List<Topic>.toStreamModelList(): List<TopicItem> =
        this.map { it.toModel() }


    private fun Topic.toModel(): TopicItem {
        return TopicItem(
            name = this.name
        )
    }

}
