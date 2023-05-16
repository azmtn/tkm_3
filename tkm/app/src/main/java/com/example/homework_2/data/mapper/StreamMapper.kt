package com.example.homework_2.data.mapper

import com.example.homework_2.data.db.model.StreamDb
import com.example.homework_2.data.mapper.TopicMapper.toStreamModelList
import com.example.homework_2.domain.model.Stream

internal object StreamMapper {
    internal fun List<Stream>.toStreamModelList(): List<StreamDb> =
        this.map { it.toStreamModel() }

    private fun Stream.toStreamModel(): StreamDb {
        return StreamDb(
            streamId = this.streamId,
            name = this.name,
            topics = this.topics.toStreamModelList(),
            isSubscribed = false
        )
    }
}
