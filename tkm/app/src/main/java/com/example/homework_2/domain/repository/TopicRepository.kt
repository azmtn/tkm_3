package com.example.homework_2.domain.repository

import com.example.homework_2.data.model.ReactionResponse
import com.example.homework_2.data.model.SendMessageResponse
import com.example.homework_2.data.model.UploadFileResponse
import com.example.homework_2.domain.model.Message
import io.reactivex.Single
import okhttp3.MultipartBody

internal interface TopicRepository {
    fun loadMessagesFromApi(
        topicName: String, anchor: Long,
        numOfMessagesInPortion: Int
    ): Single<List<Message>>

    fun sendMessage(topic: String, stream: String, content: String): Single<SendMessageResponse>

    fun addReaction(messageId: Long, emojiName: String): Single<ReactionResponse>

    fun removeReaction(messageId: Long, emojiName: String): Single<ReactionResponse>

    fun loadSingleMessageFromApi(messageId: Long): Single<Message>

    fun uploadFile(fileBody: MultipartBody.Part): Single<UploadFileResponse>

    fun loadMessagesFromDb(topicName: String): Single<List<Message>>

    fun saveMessagesToDb(messages: List<Message>)

    fun removeAllMessagesInTopicFromDb(topicName: String)

    fun removeMessagesInTopic(messageId: Long)

    fun removeMessagesInTopicFromDb(messageId: Long)

}