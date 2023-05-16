package com.example.homework_2.presentation.elm.topic

import com.example.homework_2.domain.model.Message
import okhttp3.MultipartBody

internal sealed class TopicEvent {

    sealed class Ui : TopicEvent() {

        object InitEvent : TopicEvent.Ui()

        data class LoadLastMessages(
            val topicName: String,
            val anchor: Long
        ) : TopicEvent.Ui()

        data class LoadPortionOfMessages(
            val topicName: String,
            val anchor: Long
        ) : TopicEvent.Ui()

        data class SendMessage(
            val topicName: String,
            val streamName: String,
            val content: String
        ) : TopicEvent.Ui()

        data class AddReaction(
            val messageId: Long,
            val emojiName: String,
            val emojiCode: String
        ) : TopicEvent.Ui()

        data class RemoveReaction(
            val messageId: Long,
            val emojiName: String,
            val emojiCode: String
        ) : TopicEvent.Ui()

        data class UploadFile(
            val fileName: String,
            val fileBody: MultipartBody.Part
        ) : TopicEvent.Ui()

    }

    sealed class Internal : TopicEvent() {

        data class LastMessagesLoaded(
            val items: List<Message>,
            val topicName: String
        ) : Internal()

        data class PortionOfMessagesLoaded(
            val items: List<Message>,
            val topicName: String
        ) : Internal()

        data class MessageLoaded(
            val item: Message
        ) : Internal()

        object MessageSent : Internal()

        data class ReactionAdded(
            val messageId: Long
        ) : Internal()

        data class ReactionRemoved(
            val messageId: Long
        ) : Internal()

        data class MessagesLoadingError(val error: Throwable) : Internal()

        data class MessageSendingError(val error: Throwable) : Internal()

        data class FileUploaded(
            val fileName: String,
            val fileUri: String
        ) : Internal()

        data class FileUploadingError(
            val error: Throwable,
            val fileName: String,
            val fileBody: MultipartBody.Part
        ) :Internal()
    }
}
