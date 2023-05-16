package com.example.homework_2.data.mapper

import com.example.homework_2.data.db.model.SubscribedDb
import com.example.homework_2.data.mapper.TopicItemMapper.toModelList
import com.example.homework_2.domain.model.Stream

internal object SubscribedDbMapper {
    internal fun List<SubscribedDb>.toModelList(): List<Stream> =
        this.map { it.toModel() }

    private fun SubscribedDb.toModel(): Stream {
        return Stream(
            streamId = this.streamId,
            name = this.name,
            topics = this.topics.toModelList()
        )
    }
}
