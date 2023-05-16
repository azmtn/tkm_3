package com.example.homework_2.presentation.elm.topic

import okhttp3.MultipartBody

internal sealed class TopicEffect {

    object MessageSentEffect : TopicEffect()

    data class MessagesLoadingError(val error: Throwable) : TopicEffect()

    data class MessageSendingError(val error: Throwable) : TopicEffect()

    data class FileUploadedEffect(
        val fileName: String,
        val fileUri: String
    ) : TopicEffect()

    data class FileUploadingError(
        val error: Throwable,
        val fileName: String,
        val fileBody: MultipartBody.Part,
    ) : TopicEffect()

}
