package com.example.homework_2.presentation

import com.example.homework_2.R
import com.example.homework_2.Utils.Companion.showSnackBarWithRetryAction
import com.example.homework_2.domain.model.Stream
import com.example.homework_2.presentation.adapter.StreamListAdapter
import com.example.homework_2.presentation.elm.stream.StreamsEffect
import com.example.homework_2.presentation.elm.stream.StreamsEvent
import com.google.android.material.snackbar.Snackbar

internal class AllStreamsFragment : StreamsListFragment() {

    override var initEvent: StreamsEvent = StreamsEvent.Ui.LoadAllStreamsList
    private var initShimmer = false

    override fun handleEffect(effect: StreamsEffect) {
        super.handleEffect(effect)
        if (effect is StreamsEffect.StreamsListLoadError) {
            binding.root.showSnackBarWithRetryAction(
                resources.getString(R.string.channels_error),
                Snackbar.LENGTH_LONG
            ) { store.accept(StreamsEvent.Ui.LoadAllStreamsList) }
        }
    }

    internal fun updateStreams(newStreams: List<Stream>) {
        (binding.channelsList.adapter as StreamListAdapter).apply {
            updateShimmer(initShimmer)
            updateChannels(newStreams)
            notifyDataSetChanged()
        }
    }
}
