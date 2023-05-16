package com.example.homework_2.data

import com.example.homework_2.data.db.AppDatabase
import com.example.homework_2.data.mapper.MessageDbMapper.toModelList
import com.example.homework_2.data.mapper.MessageItemMapper.toModel
import com.example.homework_2.data.mapper.MessageItemMapper.toModelList
import com.example.homework_2.data.mapper.MessageMapper.toModelList
import com.example.homework_2.data.model.ReactionResponse
import com.example.homework_2.data.model.SendMessageResponse
import com.example.homework_2.data.model.UploadFileResponse
import com.example.homework_2.data.networking.NarrowRequest
import com.example.homework_2.data.networking.ZulipApi
import com.example.homework_2.domain.repository.TopicRepository
import com.example.homework_2.domain.model.Message
import com.example.homework_2.presentation.TopicActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

internal class TopicRepositoryImpl @Inject constructor(
    private val getZulipApi: ZulipApi,
    private val db: AppDatabase
) : TopicRepository {

    override fun loadMessagesFromApi(
        topicName: String,
        anchor: Long,
        numOfMessagesInPortion: Int
    ): Single<List<Message>> {
        return getZulipApi.getMessages(
            numBefore = numOfMessagesInPortion,
            anchor = anchor.toString(),
            narrow = arrayOf(
                NarrowRequest(
                    operator = TopicActivity.TOPIC_KEY,
                    operand = topicName
                )
            ).contentToString()
        )
            .map { it.messages.toModelList() }
    }

    override fun sendMessage(
        topic: String,
        stream: String,
        content: String
    ): Single<SendMessageResponse> {
        return getZulipApi.sendMessage(
            to = stream,
            content = content,
            topic = topic
        )
    }

    override fun addReaction(messageId: Long, emojiName: String): Single<ReactionResponse> {
        return getZulipApi.addReaction(
            messageId = messageId,
            emojiName = emojiName
        )
    }

    override fun removeReaction(messageId: Long, emojiName: String): Single<ReactionResponse> {
        return getZulipApi.removeReaction(
            messageId = messageId,
            emojiName = emojiName
        )
    }

    override fun loadSingleMessageFromApi(messageId: Long): Single<Message> {
        return getZulipApi.loadSingleMessage(messageId)
            .map {it.message.toModel()}
    }

    override fun loadMessagesFromDb(topicName: String): Single<List<Message>> {
        return db.messageDao().getMessageByName(topicName)
            .onErrorReturn {
                emptyList()
            }
            .map { it.toModelList() }
    }

    override fun removeAllMessagesInTopicFromDb(topicName: String) {
        db.messageDao().deleteMessage(topicName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorComplete {
                true
            }
            .subscribe()
    }

    override fun removeMessagesInTopic(messageId: Long) {
        getZulipApi.removeMessage(
            messageId = messageId)
    }

    override fun removeMessagesInTopicFromDb(messageId: Long) {
        db.messageDao().deleteMessageById(messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorComplete {
                true
            }
            .subscribe()
    }

    override fun saveMessagesToDb(messages: List<Message>) {
        db.messageDao().saveMessage(messages.toModelList())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn {
                emptyList()
            }
            .subscribe()
    }
    override fun uploadFile(fileBody: MultipartBody.Part): Single<UploadFileResponse> {
        return getZulipApi.uploadFile(fileBody)
    }
}