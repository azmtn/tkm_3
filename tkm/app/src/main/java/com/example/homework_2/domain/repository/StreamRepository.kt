package com.example.homework_2.domain.repository

import com.example.homework_2.domain.model.Stream
import io.reactivex.Single

internal interface StreamRepository {
    fun loadStreamsFromApi(isSubscribedStreams: Boolean): Single<List<Stream>>

    fun loadStreamsFromDb(isSubscribedStreams: Boolean): Single<List<Stream>>

    fun saveStreamsToDb(streams: List<Stream>, isSubscribedStreams: Boolean)
}