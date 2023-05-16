package com.example.homework_2.data.mapper

import com.example.homework_2.data.db.model.ReactionCounterItemDbModel
import com.example.homework_2.data.model.ReactionCounterItem

internal object ReactionDbMapper {

    internal fun List<ReactionCounterItemDbModel>.toModelList(): List<ReactionCounterItem> =
        this.map { it.toModel() }

    private fun ReactionCounterItemDbModel.toModel(): ReactionCounterItem {
        return ReactionCounterItem(
            code = this.code,
            count = this.count,
            selectedByCurrentUser = this.selectedByCurrentUser
        )
    }
}
