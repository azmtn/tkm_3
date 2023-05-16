package com.example.homework_2.presentation.adapter

import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homework_2.R
import com.example.homework_2.Utils.Companion.getDateTimeFromTimestamp
import com.example.homework_2.data.model.SELF_USER_ID
import com.example.homework_2.data.networking.ZulipApi.Companion.LAST_MESSAGES
import com.example.homework_2.domain.model.Message
import com.example.homework_2.presentation.custom.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class MessageListAdapter(
    private val dialog: ReactionBottomSheetDialog,
    private val topicRecycler: RecyclerView,
    private val reactionClickListener: OnReactionClickListener,
) : RecyclerView.Adapter<BaseViewHolder>() {

    private var channelName = ""
    private var topicName = ""
    private var anchor = LAST_MESSAGES

    fun getChannelName() = channelName
    fun getTopicName() = topicName
    internal fun getAnchor() = anchor

    private var messages: List<Message> = mutableListOf()
        set(value) {
            field = value
            messagesWithDateSeparators = initDateSeparators(value)
        }

    internal fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
    }

    internal fun updateAnchor(newAnchor: Long) {
        anchor = newAnchor
    }

    internal fun updateChannelName(newChannelName: String) {
        channelName = newChannelName
    }

    internal fun updateTopicName(newTopicName: String) {
        topicName = newTopicName
    }

    private var messagesWithDateSeparators: List<Any>
        set(value) {
            if (messages.isNotEmpty() && value.isNotEmpty() && messagesWithDateSeparators.isNotEmpty()
                && messagesWithDateSeparators.last() != value.last()
            ) {
                differ.submitList(value) {
                    topicRecycler.scrollToPosition(value.size - 1)
                }
            } else {
                differ.submitList(value)
            }
        }
        get() = differ.currentList

    private val differ = AsyncListDiffer(this, MessageListDiffCallback())

    override fun getItemCount(): Int = messagesWithDateSeparators.size

    override fun getItemViewType(position: Int): Int {
        val message = messagesWithDateSeparators[position]
        return when {
            message is LocalDate -> VIEW_TYPE_DATE
            message is Message && message.userId == SELF_USER_ID -> VIEW_TYPE_OWN_MESSAGE
            else -> VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_OTHER_MESSAGE -> {
                val messageView = MessageViewGroup(parent.context)
                messageView.setOnLongClickListener {
                    return@setOnLongClickListener messageOnClickFunc(dialog, messageView)
                }
                MessageViewHolder(messageView)
            }
            VIEW_TYPE_OWN_MESSAGE -> {
                val selfMessageView = OwnMessageViewGroup(parent.context)
                changeParams(selfMessageView)
                selfMessageView.setOnLongClickListener {
                    return@setOnLongClickListener messageOnClickFunc(dialog, selfMessageView)
                }
                SelfMessageViewHolder(selfMessageView)
            }
            VIEW_TYPE_DATE -> {
                val sendDateView = LayoutInflater.from(parent.context).inflate(
                    R.layout.view_send_date,
                    parent,
                    false
                ) as FrameLayout
                SendDateViewHolder(sendDateView)
            }
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
    }

    private fun changeParams(selfMessageView: OwnMessageViewGroup) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.END
        selfMessageView.layoutParams = layoutParams
    }


    override fun onBindViewHolder(viewHolder: BaseViewHolder, position: Int) {
        return when (viewHolder) {
            is MessageViewHolder -> viewHolder.bind(messagesWithDateSeparators[position] as Message)
            is SelfMessageViewHolder -> viewHolder.bind(messagesWithDateSeparators[position] as Message)
            is SendDateViewHolder -> viewHolder.bind(messagesWithDateSeparators[position] as LocalDate)
            else -> throw RuntimeException("Unknown view type: $viewHolder")
        }
    }

    inner class SelfMessageViewHolder(private val selfMessageView: OwnMessageViewGroup) :
        BaseViewHolder(selfMessageView) {
        private val messageView = selfMessageView.binding.message
        private val flexBox = selfMessageView.binding.flexBox

        fun bind(message: Message) {
            selfMessageView.messageId = message.id
            messageView.text = Html.fromHtml(message.content, Html.FROM_HTML_MODE_COMPACT)
            fillEmojiBox(message, flexBox)
        }
    }

    inner class MessageViewHolder(private val messageView: MessageViewGroup) :
        BaseViewHolder(messageView) {
        private val avatar = messageView.binding.avatar
        private val username = messageView.binding.userName
        private val messageText = messageView.binding.message
        private val flexBox = messageView.binding.flexBox

        internal fun bind(message: Message) {
            messageView.messageId = message.id
            username.text = message.userFullName
            messageText.text = Html.fromHtml(message.content, Html.FROM_HTML_MODE_COMPACT)
            if (message.avatarUrl != null) {
                Glide.with(messageText)
                    .asBitmap()
                    .load(message.avatarUrl)
                    .circleCrop()
                    .error(R.drawable.default_avatar)
                    .into(avatar)
            }
            fillEmojiBox(message, flexBox)
        }
    }

    class SendDateViewHolder(private val sendDateView: FrameLayout) : BaseViewHolder(sendDateView) {
        internal fun bind(sendDate: LocalDate?) {
            (sendDateView.getChildAt(0) as TextView).text =
                sendDate?.format(DateTimeFormatter.ofPattern("dd MMM"))
        }
    }

    private fun initDateSeparators(messages: List<Message>): List<Any> {
        val messagesWithDateSeparators = mutableListOf<Any>()
        for (curIndex in messages.indices) {
            val curDate = getDateTimeFromTimestamp(messages[curIndex].timestamp).toLocalDate()
            if (curIndex == 0) {
                messagesWithDateSeparators.add(curDate)
            } else {
                val prevDate =
                    getDateTimeFromTimestamp(messages[curIndex - 1].timestamp).toLocalDate()
                if (prevDate != curDate) {
                    messagesWithDateSeparators.add(curDate)
                }
            }
            messagesWithDateSeparators.add(messages[curIndex])
        }
        return messagesWithDateSeparators
    }

    private fun messageOnClickFunc(dialog: ReactionBottomSheetDialog, view: View): Boolean {
        dialog.show(view)
        return true
    }

    private fun fillEmojiBox(message: Message, flexBox: FlexBoxLayout) {
        flexBox.removeAllViews()
        val addEmojiView = LayoutInflater.from(flexBox.context).inflate(
            R.layout.view_image_add_emoji,
            flexBox,
            false
        ) as ImageView
        addEmojiView.setOnClickListener {
            this@MessageListAdapter.dialog.show(addEmojiView)
        }
        flexBox.addView(addEmojiView)

        if (message.emojis.isNotEmpty()) {
            message.emojis.forEach { emoji ->
                val emojiView = ReactionView.createEmojiWithCountView(
                    flexBox = flexBox,
                    reactionCounterItem = emoji,
                    messageId = message.id,
                    reactionClickListener = reactionClickListener
                )
                if (emoji.selectedByCurrentUser) emojiView.isSelected = true
                flexBox.addView(emojiView, flexBox.childCount - 1)
            }
            addEmojiView.visibility = View.VISIBLE
        }
    }

    companion object {
        const val VIEW_TYPE_OTHER_MESSAGE = 1
        const val VIEW_TYPE_OWN_MESSAGE = 0
        const val VIEW_TYPE_DATE = 2
    }
}