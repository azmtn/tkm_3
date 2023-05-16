package com.example.homework_2.data.mapper

import com.example.homework_2.data.db.model.StreamDb
import com.example.homework_2.data.mapper.TopicItemMapper.toModelList
import com.example.homework_2.domain.model.Stream

internal object StreamDbMapper {
    internal fun List<StreamDb>.toModelList(): List<Stream> =
        this.map { it.toModel() }

    private fun StreamDb.toModel(): Stream {
        return Stream(
            streamId = this.streamId,
            name = this.name,
            topics = this.topics.toModelList()
        )
    }
}
