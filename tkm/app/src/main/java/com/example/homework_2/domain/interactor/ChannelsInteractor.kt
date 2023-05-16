package com.example.homework_2.domain.interactor

import com.example.homework_2.domain.repository.StreamRepository
import com.example.homework_2.domain.model.Stream
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

internal class ChannelsInteractor(
    private val streamsRepository: StreamRepository
) {

    private val queryEvents: PublishSubject<String> = PublishSubject.create()

    fun loadStreams(isSubscribedStreams: Boolean): Observable<List<Stream>> {
        return Single.merge(
            streamsRepository.loadStreamsFromDb(isSubscribedStreams),
            streamsRepository.loadStreamsFromApi(isSubscribedStreams)
                .doOnSuccess {
                    streamsRepository.saveStreamsToDb(it, isSubscribedStreams)
                }
        ).toObservable()
            .subscribeOn(Schedulers.io())
    }

    fun processSearchQuery(query: String) = queryEvents.onNext(query)

    fun subscribeOnSearchStreamsEvents(): Observable<List<Stream>> {
        return queryEvents
            .map { query -> query.trim() }
            .distinctUntilChanged()
            .debounce(DELAY_BETWEEN_INPUT, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { query ->
                searchStreamsByQuery(query)
            }
    }

    private fun searchStreamsByQuery(query: String): Observable<List<Stream>> {
        return loadStreams(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.filter { stream ->
                    stream.name.lowercase().contains(query.lowercase())
                }
            }
    }

    companion object {
        const val DELAY_BETWEEN_INPUT = 1000L
    }
}
