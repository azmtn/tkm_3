package com.example.homework_2.ui

import android.view.View
import com.example.homework_2.R
import com.example.homework_2.presentation.SubscribedFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import org.hamcrest.Matcher

internal object SubscribedStream : KScreen<SubscribedStream>() {
    override val layoutId: Int = R.layout.fragment_streams
    override val viewClass: Class<*> = SubscribedFragment::class.java

    val streamsList = KRecyclerView(
        { withId(R.id.channels_list) },
        { itemType { StreamItem(it) } }
    )

    val topicList = KRecyclerView(
        { withId(R.id.streams_list) },
        { itemType { TopicItem(it) } }
    )

    class StreamItem(parent: Matcher<View>) : KRecyclerItem<StreamItem>(parent) {

        val arrowIcon = KImageView(parent) { withId(R.id.arrow_icon) }
        val streamList = KRecyclerView(
            parent,
            { withId(R.id.streams_list) },
            { itemType { TopicItem(parent) } }
        )
    }

    class TopicItem(parent: Matcher<View>) : KRecyclerItem<TopicItem>(parent){
        val topic = KImageView(parent) { withId(com.example.homework_2.R.id.channels_list) }

        val topicList = KRecyclerView(
            parent,
            {withId(R.id.topic_item)},
            {itemType { StreamItem(parent) }
            }
        )
    }


}