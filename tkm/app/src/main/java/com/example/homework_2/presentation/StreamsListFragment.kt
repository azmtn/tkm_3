package com.example.homework_2.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_2.App
import com.example.homework_2.R
import com.example.homework_2.databinding.FragmentStreamsBinding
import com.example.homework_2.di.ActivityScope
import com.example.homework_2.di.component.DaggerStreamsComponent
import com.example.homework_2.domain.model.Topic
import com.example.homework_2.presentation.adapter.OnTopicItemClickListener
import com.example.homework_2.presentation.adapter.StreamListAdapter
import com.example.homework_2.presentation.elm.stream.StreamsEffect
import com.example.homework_2.presentation.elm.stream.StreamsElmStoreFactory
import com.example.homework_2.presentation.elm.stream.StreamsEvent
import com.example.homework_2.presentation.elm.stream.StreamsState
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

@ActivityScope
internal abstract class StreamsListFragment
    : ElmFragment<StreamsEvent, StreamsEffect, StreamsState>(), OnTopicItemClickListener {

    @Inject
    internal lateinit var streamsElmStoreFactory: StreamsElmStoreFactory

    lateinit var binding: FragmentStreamsBinding
    protected lateinit var adapter: StreamListAdapter
    private lateinit var channelsListRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StreamListAdapter(this)
        binding.channelsList.adapter = adapter
        channelsListRecyclerView = binding.channelsList
        val layoutManager = LinearLayoutManager(this.context)
        channelsListRecyclerView.layoutManager = layoutManager
        channelsListRecyclerView.adapter = adapter
        channelsListRecyclerView.smoothScrollToPosition(adapter.itemCount)
    }

    override fun createStore(): Store<StreamsEvent, StreamsEffect, StreamsState> {
        val streamsComponent = DaggerStreamsComponent.factory().create(
            (activity?.application as App).applicationComponent
        )
        streamsComponent.inject(this)
        return streamsElmStoreFactory.provide()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun render(state: StreamsState) {
        with(adapter) {
            updateShimmer(state.isLoading)
            updateChannels(state.items)
            updateIsOpened(state.isTopicOpen)
            notifyDataSetChanged()
        }
    }

    override fun handleEffect(effect: StreamsEffect) {
        super.handleEffect(effect)
        if (effect is StreamsEffect.NavigateToTopic) {
            NavHostFragment.findNavController(binding.root.findFragment())
                .navigate(R.id.action_nav_channels_to_nav_chat, effect.bundle)
        }
    }

    override fun topicItemClickListener(topic: Topic, channelName: String) {
        val bundle = bundleOf(
            TopicActivity.CHANNEL_NAME to channelName,
            TopicActivity.TOPIC_NAME to topic.name
        )
        store.accept(StreamsEvent.Ui.LoadTopic(bundle))
    }
}
