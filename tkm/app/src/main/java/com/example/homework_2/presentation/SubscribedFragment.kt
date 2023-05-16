package com.example.homework_2.presentation

import com.example.homework_2.R
import com.example.homework_2.Utils.Companion.showSnackBarWithRetryAction
import com.example.homework_2.presentation.elm.stream.StreamsEffect
import com.example.homework_2.presentation.elm.stream.StreamsEvent
import com.google.android.material.snackbar.Snackbar

internal class SubscribedFragment : StreamsListFragment() {

    override var initEvent: StreamsEvent = StreamsEvent.Ui.LoadSubscribedStreamsList

    override fun handleEffect(effect: StreamsEffect) {
        super.handleEffect(effect)
        if (effect is StreamsEffect.StreamsListLoadError) {
            binding.root.showSnackBarWithRetryAction(
                resources.getString(R.string.channels_error),
                Snackbar.LENGTH_LONG
            ) { store.accept(StreamsEvent.Ui.LoadSubscribedStreamsList) }
        }
    }
}