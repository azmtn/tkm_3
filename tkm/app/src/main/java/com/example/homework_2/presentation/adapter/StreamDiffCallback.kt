package com.example.homework_2.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.homework_2.domain.model.Stream
import com.example.homework_2.domain.model.Topic

class StreamDiffCallback : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem is Stream && newItem is Stream) {
            return oldItem.name == newItem.name
        }
        if (oldItem is Topic && newItem is Topic) {
            return oldItem.name == newItem.name
        }
        return false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem is Stream && newItem is Stream) return oldItem == newItem
        if (oldItem is Topic && newItem is Topic) return oldItem == newItem
        return false
    }
}
