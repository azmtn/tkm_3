package com.example.homework_2.data.db

import androidx.room.TypeConverter
import com.example.homework_2.data.db.model.ReactionCounterItemDbModel
import com.example.homework_2.data.model.TopicItem
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class Converters {

    @TypeConverter
    fun fromTopics(topics: List<TopicItem>): String = Json.encodeToString(topics)

    @TypeConverter
    fun toTopics(topicsStr: String): List<TopicItem> = Json.decodeFromString(topicsStr)

    @TypeConverter
    fun fromReaction(reaction: List<ReactionCounterItemDbModel>): String = Json.encodeToString(reaction)

    @TypeConverter
    fun toReaction(reactionStr: String): List<ReactionCounterItemDbModel> = Json.decodeFromString(reactionStr)

}
