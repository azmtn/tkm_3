package com.example.homework_2.presentation.adapter

import com.example.homework_2.domain.model.Topic

internal interface OnTopicItemClickListener {

    fun topicItemClickListener(topic: Topic, channelName: String)

}
