package com.example.homework_2.data.mapper

import com.example.homework_2.data.mapper.TopicItemMapper.toModelList
import com.example.homework_2.data.model.StreamItem
import com.example.homework_2.domain.model.Stream

internal object StreamItemMapper {

    internal fun StreamItem.toModel(): Stream {
        return Stream(
            streamId = this.streamId,
            name = this.name,
            topics = this.topicItems.toModelList()
        )
    }
}
