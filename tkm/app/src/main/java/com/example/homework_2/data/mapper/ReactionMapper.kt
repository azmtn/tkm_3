package com.example.homework_2.data.mapper

import com.example.homework_2.data.db.model.ReactionCounterItemDbModel
import com.example.homework_2.data.model.ReactionCounterItem

internal object ReactionMapper {

    internal fun List<ReactionCounterItem>.toModelList(): List<ReactionCounterItemDbModel> =
        this.map { it.toModel() }

    private fun ReactionCounterItem.toModel(): ReactionCounterItemDbModel {
        return ReactionCounterItemDbModel(
            code = this.code,
            count = this.count,
            selectedByCurrentUser = this.selectedByCurrentUser
        )
    }
}
