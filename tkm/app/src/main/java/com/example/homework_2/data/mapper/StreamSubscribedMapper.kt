package com.example.homework_2.data.mapper

import com.example.homework_2.data.db.model.SubscribedDb
import com.example.homework_2.data.mapper.TopicMapper.toStreamModelList
import com.example.homework_2.domain.model.Stream

internal object StreamSubscribedMapper {
    internal fun List<Stream>.toSubscribedModelList(): List<SubscribedDb> =
        this.map { it.toSubscribedModel() }

    private fun Stream.toSubscribedModel(): SubscribedDb {
        return SubscribedDb(
            streamId = this.streamId,
            name = this.name,
            topics = this.topics.toStreamModelList(),
            isSubscribed = false
        )
    }
}
