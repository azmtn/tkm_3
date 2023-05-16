package com.example.homework_2.domain.interactor

import android.util.Log
import com.example.homework_2.domain.repository.PeopleRepository
import com.example.homework_2.domain.model.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


internal class PeopleInteractor (
    private val peopleRepository: PeopleRepository
) {

   private val queryEvents: PublishSubject<String> = PublishSubject.create()

    fun loadUsers(): Observable<List<User>> {
        return Single.merge(
            peopleRepository.loadUsersFromDb(),
            peopleRepository.loadUsersFromApi()
                .doOnSuccess {
                    peopleRepository.saveUsersToDb(it.sortedBy { it.fullName })
                }
                .doOnError {
                    Log.e("PeopleInteractor", "Users error", it)
            }
        ).toObservable()
            .subscribeOn(Schedulers.io())
            .doOnError {
                Log.e("PeopleInteractor", "Users error", it)
            }
    }

    fun processSearchQuery(query: String) = queryEvents.onNext(query)

    fun searchEvents(): Observable<List<User>> {
        return queryEvents
            .map { query -> query.trim() }
            .distinctUntilChanged()
            .debounce(ChannelsInteractor.DELAY_BETWEEN_INPUT, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { query ->
                searchByQuery(query)
            }
    }

    private fun searchByQuery(query: String): Observable<List<User>> {
        return loadUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.filter { user ->
                    user.fullName?.lowercase()!!.contains(query.lowercase())
                }
            }
    }
}
