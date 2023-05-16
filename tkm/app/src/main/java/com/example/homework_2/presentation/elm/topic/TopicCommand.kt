package com.example.homework_2.presentation.elm.topic

import okhttp3.MultipartBody

internal sealed class TopicCommand {

    data class LoadLastMessages(
        val topicName: String,
        val anchor: Long,
        val isFirstPosition: Boolean = false
    ) : TopicCommand()

    data class LoadPortionOfMessages(
        val topicName: String,
        val anchor: Long
    ) : TopicCommand()

    data class LoadMessage(
        val messageId: Long
    ) : TopicCommand()

    data class SendMessage(
        val topicName: String,
        val streamName: String,
        val content: String
    ) : TopicCommand()

    data class AddReaction(
        val messageId: Long,
        val emojiName: String
    ) : TopicCommand()

    data class RemoveReaction(
        val messageId: Long,
        val emojiName: String
    ) : TopicCommand()

    data class UploadFile(
        val fileName: String,
        val fileBody: MultipartBody.Part
    ) : TopicCommand()

}
