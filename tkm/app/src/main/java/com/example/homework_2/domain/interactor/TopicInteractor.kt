package com.example.homework_2.domain.interactor

import com.example.homework_2.data.model.ReactionResponse
import com.example.homework_2.data.model.SendMessageResponse
import com.example.homework_2.data.model.UploadFileResponse
import com.example.homework_2.domain.repository.TopicRepository
import com.example.homework_2.domain.model.Message
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

internal class TopicInteractor(
    private val topicRepository: TopicRepository
) {

    fun loadLastMessages(
        topicName: String,
        anchor: Long
    ): Observable<List<Message>> {
        return Observable.merge(
            topicRepository.loadMessagesFromDb(topicName)
                .toObservable(),
            topicRepository.loadMessagesFromApi(
                topicName,
                anchor,
                NUMBER_OF_MESSAGES
            )
                .doOnSuccess {
                    if (it.isNotEmpty()) {
                        cacheMessages(it, topicName)
                    }
                }
                .toObservable()
        )
            .subscribeOn(Schedulers.io())
    }
    private fun cacheMessages(
        messages: List<Message>,
        topicName: String
    ) {
        topicRepository.removeAllMessagesInTopicFromDb(topicName)
        topicRepository.saveMessagesToDb(messages.takeLast(MAX_MESSAGES_IN_CACHE))
    }


    fun loadPortionOfMessages(
        topicName: String,
        anchor: Long
    ): Observable<List<Message>> {
        return topicRepository.loadMessagesFromApi(
            topicName,
            anchor,
            NUMBER_OF_MESSAGES
        )
            .toObservable()
            .subscribeOn(Schedulers.io())
    }

    fun loadMessage(messageId: Long): Observable<Message> {
        return topicRepository.loadSingleMessageFromApi(messageId)
            .toObservable()
            .subscribeOn(Schedulers.io())
    }

    fun sendMessage(
        topic: String,
        stream: String,
        content: String
    ): Single<SendMessageResponse> {
        return topicRepository.sendMessage(topic, stream, content)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addReaction(
        messageId: Long,
        emojiName: String
    ): Single<ReactionResponse> {
        return topicRepository.addReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun removeReaction(
        messageId: Long,
        emojiName: String
    ): Single<ReactionResponse> {
        return topicRepository.removeReaction(messageId, emojiName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun uploadFile(fileBody: MultipartBody.Part): Single<UploadFileResponse> {
        return topicRepository.uploadFile(fileBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private const val NUMBER_OF_MESSAGES = 20
        private const val MAX_MESSAGES_IN_CACHE = 50
    }
}
