package com.example.homework_2.data

import android.util.Log
import com.example.homework_2.data.db.AppDatabase
import com.example.homework_2.data.mapper.StreamDbMapper.toModelList
import com.example.homework_2.data.mapper.StreamItemMapper.toModel
import com.example.homework_2.data.mapper.StreamMapper.toStreamModelList
import com.example.homework_2.data.mapper.StreamSubscribedMapper.toSubscribedModelList
import com.example.homework_2.data.mapper.SubscribedDbMapper.toModelList
import com.example.homework_2.data.model.StreamItem
import com.example.homework_2.data.model.TopicsListResponse
import com.example.homework_2.data.networking.ZulipApi
import com.example.homework_2.domain.repository.StreamRepository
import com.example.homework_2.domain.model.Stream
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class StreamsRepositoryImpl @Inject constructor(
    private val getZulipApi: ZulipApi,
    private val db: AppDatabase
) : StreamRepository {

    override fun loadStreamsFromApi(isSubscribedStreams: Boolean): Single<List<Stream>> {
        val baseStream = if (isSubscribedStreams) {
            getZulipApi.getSubscribedStreams()
                .flattenAsObservable { it.subscriptions }
        } else {
            getZulipApi.getAllStreams()
                .flattenAsObservable { it.streams }
        }
        return baseStream
            .flatMapSingle { getTopicsInStream(it) }
            .doOnError {
                Log.e("StreamRepository", "Api streams error", it)
            }
            .toList()
    }

    private fun getTopicsInStream(stream: StreamItem): Single<Stream> {
        return getZulipApi.getTopicsInStream(streamId = stream.streamId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                stream.topicItems = it.topicItems
            }
            .onErrorReturn {
                TopicsListResponse(emptyList())
            }
            .map { stream.toModel() }
    }

    override fun loadStreamsFromDb(isSubscribedStreams: Boolean): Single<List<Stream>> {
        if (!isSubscribedStreams) {
            return db.streamDao().getStream()
                .map { it.toModelList() }
                .onErrorReturn {
                    emptyList()
                }
        }
        else{
            return db.subscribedDao().getSubscribed()
                .map { it.toModelList() }
                .onErrorReturn {
                    emptyList()
                }
        }
    }

    override fun saveStreamsToDb(streams: List<Stream>, isSubscribedStreams: Boolean) {
        if (!isSubscribedStreams) {
            db.streamDao().saveStream(streams.toStreamModelList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn {
                    emptyList()
                }
                .subscribe()
        } else {
            db.subscribedDao().saveSubscribed(streams.toSubscribedModelList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn {
                    emptyList()
                }
                .subscribe()
        }
    }
}
