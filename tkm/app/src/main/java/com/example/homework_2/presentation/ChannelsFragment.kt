package com.example.homework_2.presentation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment
import com.example.homework_2.App
import com.example.homework_2.R
import com.example.homework_2.databinding.FragmentChannelsBinding
import com.example.homework_2.di.component.DaggerStreamsComponent
import com.example.homework_2.presentation.adapter.ChannelsAdapter
import com.example.homework_2.presentation.elm.stream.StreamsEffect
import com.example.homework_2.presentation.elm.stream.StreamsElmStoreFactory
import com.example.homework_2.presentation.elm.stream.StreamsEvent
import com.example.homework_2.presentation.elm.stream.StreamsState
import com.google.android.material.tabs.TabLayoutMediator
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class ChannelsFragment : ElmFragment<StreamsEvent, StreamsEffect, StreamsState>() {

    @Inject
    internal lateinit var streamsElmStoreFactory: StreamsElmStoreFactory

    override var initEvent: StreamsEvent = StreamsEvent.Ui.SubscribeOnSearchStreamsEvents
    private lateinit var binding: FragmentChannelsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewPager()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.searchEditText.doAfterTextChanged { text ->
                val allStreamsTab = binding.tabLayout.getTabAt(1)
                allStreamsTab?.select()
                val query = text?.toString().orEmpty()
                store.accept(StreamsEvent.Ui.SearchStreamsByQuery(query))
            }
        }, 50)

        binding.searchIcon.setOnClickListener {
            binding.searchEditText.requestFocus()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun createStore(): Store<StreamsEvent, StreamsEffect, StreamsState> {
        val streamsComponent = DaggerStreamsComponent.factory().create(
            (activity?.application as App).applicationComponent
        )
        streamsComponent.inject(this)
        return streamsElmStoreFactory.provide()
    }

    override fun render(state: StreamsState) {
        val streamsListPagerAdapter = (binding.pager.adapter as ChannelsAdapter)

        if (streamsListPagerAdapter.isAllChannelsFragment()) {
            streamsListPagerAdapter.allStreamsFragment.updateStreams(
                state.items
            )
        }
    }

    override fun handleEffect(effect: StreamsEffect) {
        when (effect) {
            is StreamsEffect.StreamsListLoadError -> {
                store.accept(StreamsEvent.Ui.SubscribeOnSearchStreamsEvents)
                Toast.makeText(
                    context,
                    resources.getString(R.string.channels_error),
                    Toast.LENGTH_LONG
                ).show()
            }
            is StreamsEffect.NavigateToTopic -> {
                NavHostFragment.findNavController(binding.root.findFragment())
                    .navigate(R.id.action_nav_channels_to_nav_chat, effect.bundle)
            }
        }
    }

    private fun configureViewPager() {
        val viewPager = binding.pager
        val tabLayout = binding.tabLayout

        val pagerAdapter = ChannelsAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabNames = listOf(
                resources.getString(R.string.subscribed),
                resources.getString(R.string.all_streams)
            )
            tab.text = tabNames[position]
        }.attach()
    }
}
