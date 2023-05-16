package com.example.homework_2.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_2.R
import com.example.homework_2.databinding.ItemStreamBinding
import com.example.homework_2.databinding.ItemTopicBinding
import com.example.homework_2.domain.model.Stream
import com.example.homework_2.domain.model.Topic
import io.reactivex.disposables.CompositeDisposable

internal class StreamListAdapter(private val topicItemClickListener: OnTopicItemClickListener) :
    RecyclerView.Adapter<BaseViewHolder>() {

    private var initShimmer = true
    private var compositeDisposable = CompositeDisposable()
    private var currentStreamName = ""
    private var isOpened = true

    internal fun updateIsOpened(newIsOpened: Boolean) {
        isOpened = newIsOpened
    }

    private val differ = AsyncListDiffer(this, StreamDiffCallback())

    private var channels: List<Any>
        set(value) = differ.submitList(value)
        get() = differ.currentList

    internal fun updateChannels(newChannels: List<Any>) {
        channels = newChannels
    }

    internal fun updateShimmer(newShimmer: Boolean) {
        initShimmer = newShimmer
    }

    override fun getItemCount(): Int {
        return if (initShimmer) SHIMMER_COUNT else channels.size
    }

    override fun getItemViewType(position: Int): Int {
        if (initShimmer) return VIEW_TYPE_STREAM
        return when (channels[position]) {
            is Stream -> VIEW_TYPE_STREAM
            is Topic -> VIEW_TYPE_TOPIC
            else -> throw RuntimeException("Unknown view type: $position")
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.dispose()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_STREAM -> {
                val itemStreamView = ItemStreamBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                StreamViewHolder(itemStreamView)
            }
            VIEW_TYPE_TOPIC -> {
                val topicItemBinding = ItemTopicBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                TopicViewHolder(topicItemBinding)
            }
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(viewHolder: BaseViewHolder, position: Int) {
        when (viewHolder) {
            is StreamViewHolder -> {
                if (initShimmer) {
                    viewHolder.shimmedText.visibility = View.VISIBLE
                    viewHolder.shimmerFrameLayout.startShimmer()
                } else {
                    viewHolder.shimmerFrameLayout.stopShimmer()
                    viewHolder.shimmerFrameLayout.setShimmer(null)
                    viewHolder.shimmedText.visibility = View.GONE
                    viewHolder.channelName.visibility = View.VISIBLE
                    val channel = channels[position] as Stream
                    viewHolder.initChannelListener(channel)
                    viewHolder.bind(channel)
                }
            }
            is TopicViewHolder -> {
                if (initShimmer) {
                    viewHolder.shimmerFrameLayout.startShimmer()
                } else {
                    viewHolder.shimmerFrameLayout.stopShimmer()
                    viewHolder.shimmerFrameLayout.setShimmer(null)
                    viewHolder.topicItem.foreground = null

                    viewHolder.bind(channels[position] as Topic)
                }
            }
        }
    }

    inner class TopicViewHolder(private val binding: ItemTopicBinding) :
        BaseViewHolder(binding.root) {
        internal val shimmerFrameLayout = binding.shimmerLayout
        internal val topicItem = binding.topicItem

        @SuppressLint("ResourceAsColor")
        fun bind(topic: Topic) {
            binding.topicName.text = topic.name
            if (adapterPosition % 2 == 0)
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        TOPIC_BACKGROUND_COLOR
                    )
                )
            else
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        TOPIC_ODD_BACKGROUND_COLOR
                    )
                )
            binding.root.setOnClickListener {
                this@StreamListAdapter.topicItemClickListener.topicItemClickListener(
                    topic, currentStreamName
                )
            }
        }
    }

    inner class StreamViewHolder(private val binding: ItemStreamBinding) :
        BaseViewHolder(binding.root) {
        val channelName = binding.channelName
        private val arrowIcon = binding.arrowIcon
        internal val shimmedText = binding.shimmedText
        internal val shimmerFrameLayout = binding.shimmerLayout

        fun bind(streamItem: Stream) {
            channelName.text =
                binding.root.resources.getString(R.string.stream_name, streamItem.name)
        }

        fun initChannelListener(stream: Stream) {
            binding.root.setOnClickListener {
                if (!isOpened) {
                    initShimmer = false
                    currentStreamName = stream.name
                    setTopics(stream.topics, adapterPosition)
                    arrowIcon.setImageResource(R.drawable.arrow_up)
                    isOpened = true
                } else {
                    initShimmer = false
                    if (channels.size > stream.topics.size) {
                        deleteTopics(adapterPosition, stream.topics.size)
                    }
                    arrowIcon.setImageResource(R.drawable.arrow_down)
                    isOpened = false
                }
            }
        }
    }

    private fun deleteTopics(position: Int, size: Int) {
        val listChannels: MutableList<Any> = channels.toMutableList()
        for (i in 0 until size) {
            listChannels.removeAt(position + 1)
        }
        channels = listChannels
    }

    private fun setTopics(list: List<Any>, position: Int) {
        val listChannels: MutableList<Any> = channels.toMutableList()
        listChannels.addAll(position + 1, list)
        channels = listChannels

    }

    companion object {
        const val VIEW_TYPE_STREAM = 0
        const val VIEW_TYPE_TOPIC = 1
        const val SHIMMER_COUNT = 8
        const val TOPIC_BACKGROUND_COLOR = R.color.teal_700
        const val TOPIC_ODD_BACKGROUND_COLOR = R.color.sepia
    }
}