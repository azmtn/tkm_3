package com.example.homework_2.data.networking

import com.example.homework_2.data.model.*
import com.example.homework_2.data.model.UserPresenceResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

internal interface ZulipApi {

    @GET("api/v1/streams")
    fun getAllStreams(): Single<AllChannelsListResponse>

    @GET("api/v1/users/me/subscriptions")
    fun getSubscribedStreams(): Single<SubscribedChannelsListResponse>

    @GET("api/v1/users/me/{stream_id}/topics")
    fun getTopicsInStream(
        @Path("stream_id") streamId: Long
    ): Single<TopicsListResponse>

    @GET("api/v1/users")
    fun getAllUsers(): Single<AllUsersListResponse>

    @GET("api/v1/users/{user_id_or_email}/presence")
    fun getUserPresence(
        @Path("user_id_or_email") userIdOrEmail: String
    ): Single<UserPresenceResponse>

    @GET("api/v1/users/me")
    fun getOwnUser(): Single<UserItem>

    @GET("api/v1/messages")
    fun getMessages(
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int = NUMBER_BEFORE,
        @Query("anchor") anchor: String = "first_unread",
        @Query(value = "narrow", encoded = true) narrow: String
    ): Single<MessagesListResponse>

    @POST("api/v1/messages")
    fun sendMessage(
        @Query("type") type: String = "stream",
        @Query("to") to: String,
        @Query("content") content: String,
        @Query("topic") topic: String,
    ): Single<SendMessageResponse>

    @POST("api/v1/messages/{message_id}/reactions")
    fun addReaction(
        @Path("message_id") messageId: Long,
        @Query("emoji_name") emojiName: String
    ): Single<ReactionResponse>

    @DELETE("api/v1/messages/{message_id}/reactions")
    fun removeReaction(
        @Path("message_id") messageId: Long,
        @Query("emoji_name") emojiName: String
    ): Single<ReactionResponse>

    @DELETE("api/v1/messages/{message_id}")
    fun removeMessage(
        @Path("message_id") messageId: Long
    )

    @GET("api/v1/messages/{msg_id}")
    fun loadSingleMessage(
        @Path("msg_id") messageId: Long
    ): Single<LoadSingleMessageResponse>

    @Multipart
    @POST("api/v1/user_uploads")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Single<UploadFileResponse>

    companion object{
        internal const val NUMBER_BEFORE = 20
        internal const val LAST_MESSAGES = 1000000000000000000
    }
}