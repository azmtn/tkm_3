package com.example.homework_2.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homework_2.App
import com.example.homework_2.R
import com.example.homework_2.Utils
import com.example.homework_2.Utils.Companion.getFileName
import com.example.homework_2.Utils.Companion.hasPermissions
import com.example.homework_2.Utils.Companion.showSnackBarWithRetryAction
import com.example.homework_2.data.Reaction
import com.example.homework_2.databinding.ActivityTopicBinding
import com.example.homework_2.data.model.ReactionCounterItem
import com.example.homework_2.di.ActivityScope
import com.example.homework_2.di.component.DaggerTopicComponent
import com.example.homework_2.presentation.adapter.MessageListAdapter
import com.example.homework_2.presentation.adapter.OnBottomSheetListener
import com.example.homework_2.presentation.adapter.OnReactionClickListener
import com.example.homework_2.presentation.custom.*
import com.example.homework_2.presentation.elm.topic.TopicEffect
import com.example.homework_2.presentation.elm.topic.TopicElmStoreFactory
import com.example.homework_2.presentation.elm.topic.TopicEvent
import com.example.homework_2.presentation.elm.topic.TopicState
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.core.store.Store
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@ActivityScope
internal class TopicActivity : ElmActivity<TopicEvent, TopicEffect, TopicState>(),
    OnReactionClickListener, OnBottomSheetListener {

    @Inject
    internal lateinit var topicElmStoreFactory: TopicElmStoreFactory

    override var initEvent: TopicEvent = TopicEvent.Ui.InitEvent
    private lateinit var dialog: ReactionBottomSheetDialog
    private lateinit var binding: ActivityTopicBinding
    private lateinit var topicRecycler: RecyclerView
    private lateinit var adapter: MessageListAdapter
    private lateinit var streamName: String
    private lateinit var topicName: String
    private var fileResult = initFileResult()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createAndConfigureBottomSheet()
        enterMessageInput()
        setupMessageRecycler()
        setupToolbar()
    }

    override fun createStore(): Store<TopicEvent, TopicEffect, TopicState> {
        val topicComponent = DaggerTopicComponent.factory().create(
            (this.application as App).applicationComponent
        )
        topicComponent.inject(this)
        return topicElmStoreFactory.provide()
    }

    private fun setupToolbar() {
        binding.backIcon.setOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }
        binding.topicName.text = adapter.getTopicName()
        binding.channelName.text = adapter.getChannelName()
    }

    override fun render(state: TopicState) {
        binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        with(adapter) {
            updateMessages(state.items)
            updateAnchor(state.anchor)
            println("state updated")
        }
    }

    override fun handleEffect(effect: TopicEffect) {
        when (effect) {
            is TopicEffect.MessageSentEffect -> {
                binding.enterMessage.text.clear()
            }
            is TopicEffect.MessagesLoadingError -> {
                binding.root.showSnackBarWithRetryAction(
                    resources.getString(R.string.messages_error),
                    Snackbar.LENGTH_LONG
                ) { setupMessageRecycler() }
            }
            is TopicEffect.MessageSendingError -> {
                binding.root.showSnackBarWithRetryAction(
                    resources.getString(R.string.messages_error),
                    Snackbar.LENGTH_LONG
                ) { }
            }
            is TopicEffect.FileUploadedEffect -> {
                binding.enterMessage.text.append("[${effect.fileName}](${effect.fileUri})\n\n")
                binding.sendButton.showActionIcon(R.drawable.airplane, R.color.teal_700)
            }
            is TopicEffect.FileUploadingError -> {
                binding.root.showSnackBarWithRetryAction(
                    resources.getString(R.string.uploading_file_error),
                    Snackbar.LENGTH_LONG
                ) { store.accept(TopicEvent.Ui.UploadFile(effect.fileName, effect.fileBody)) }
            }
        }
    }

    override fun onReactionClick(reactionView: ReactionView) {
        val emojiName = Reaction.emoji[reactionView.reaction]
        if (emojiName != null) {
            if (!reactionView.isSelected) {
                store.accept(
                    TopicEvent.Ui.AddReaction(
                        reactionView.messageId,
                        emojiName,
                        reactionView.reaction
                    )
                )
                reactionView.isSelected = true
                reactionView.reactionCount = reactionView.reactionCount.plus(1)
            } else {
                store.accept(
                    TopicEvent.Ui.RemoveReaction(
                        reactionView.messageId,
                        emojiName,
                        reactionView.reaction
                    )
                )
                reactionView.isSelected = false
                reactionView.reactionCount = reactionView.reactionCount.minus(1)
                if (reactionView.reactionCount == 0) {
                    val emojiBox = (reactionView.parent as FlexBoxLayout)
                    emojiBox.removeView(reactionView)
                    if (emojiBox.childCount == 1) {
                        emojiBox.getChildAt(0).visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupMessageRecycler() {
        topicRecycler = binding.chat
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        topicRecycler.layoutManager = layoutManager
        adapter = MessageListAdapter(dialog, topicRecycler, this)

        topicName = intent.getStringExtra(TOPIC_NAME)?.lowercase() ?: ""
        adapter.updateTopicName(resources.getString(
            R.string.topic_name,
            topicName
        ))

        streamName = intent.getStringExtra(CHANNEL_NAME)?.lowercase() ?: ""
        adapter.updateChannelName(resources.getString(
            R.string.stream_name,
            intent.getStringExtra(CHANNEL_NAME)
        ))

        store.accept(
            TopicEvent.Ui.LoadLastMessages(
                topicName = topicName,
                anchor = adapter.getAnchor()
            )
        )

        topicRecycler.adapter = adapter

        topicRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            private var isNewLoading = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isNewLoading = false
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItemPosition == SCROLL_POSITION_FOR_NEXT && !isNewLoading) {
                    isNewLoading = true
                    store.accept(
                        TopicEvent.Ui.LoadPortionOfMessages(
                            topicName = topicName,
                            anchor = adapter.getAnchor()
                        )
                    )
                }
            }
        })
    }

    private fun enterMessageInput() {
        val enterMessage = binding.enterMessage
        val sendButton = binding.sendButton

        enterMessage.doAfterTextChanged {
            if (enterMessage.text.isEmpty()) {
                sendButton.showActionIcon(R.drawable.plus, R.color.grey_300)
            }
            if (enterMessage.text.length == 1) {
                sendButton.showActionIcon(R.drawable.airplane, R.color.teal_700)
            }
        }

        sendButton.setOnClickListener {
            if (enterMessage.text.isNotEmpty()) {
                store.accept(
                    TopicEvent.Ui.SendMessage(
                        topicName = topicName,
                        streamName = streamName,
                        content = enterMessage.text.toString()
                    )
                )
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(enterMessage.windowToken, 0)
            } else {
                if (!hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )
                } else {
                    fileResult.launch("*/*")
                }
            }
        }
    }

    private fun FloatingActionButton.showActionIcon(drawableId: Int, colorId: Int) {
        setImageResource(drawableId)
        imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, colorId))
    }

    @SuppressLint("InflateParams")
    private fun createAndConfigureBottomSheet() {
        val bottomSheetLayout =
            layoutInflater.inflate(R.layout.layout_bottom_sheet, null) as LinearLayout
        dialog = ReactionBottomSheetDialog(
            context = this,
            theme = R.style.BottomSheetDialogTheme,
            bottomSheet = bottomSheetLayout,
            bottomSheetListener = this
        )
        dialog.setContentView(bottomSheetLayout)
    }

    override fun onBottomSheetChoose(
        selectedView: View?,
        chooseReaction: String
    ) {
        val emojiBox = when (selectedView) {
            is MessageViewGroup -> selectedView.binding.flexBox
            is OwnMessageViewGroup -> selectedView.binding.flexBox
            is ImageView -> selectedView.parent as FlexBoxLayout
            else -> null
        }
        val emoji = emojiBox?.children?.firstOrNull {
            it is ReactionView && it.reaction == chooseReaction
        }
        if (emoji is ReactionView) {
            if (!emoji.isSelected) {
                emoji.isSelected = true
                emoji.reactionCount++
            }
        } else {
            addNewEmojiToMessage(selectedView, emojiBox, chooseReaction)
        }
    }

    private fun addNewEmojiToMessage(
        selectedView: View?,
        emojiBox: FlexBoxLayout?,
        chosenEmojiCode: String
    ) {
        val messageId = when (selectedView) {
            is MessageViewGroup -> selectedView.messageId
            is OwnMessageViewGroup -> selectedView.messageId
            is ImageView -> {
                when (val parentViewGroup = selectedView.parent.parent) {
                    is MessageViewGroup -> parentViewGroup.messageId
                    is OwnMessageViewGroup -> parentViewGroup.messageId
                    else -> 0L
                }
            }
            else -> 0L
        }
        if (emojiBox != null) {
            val emojiView = ReactionView.createEmojiWithCountView(
                flexBox = emojiBox,
                reactionCounterItem = ReactionCounterItem(chosenEmojiCode, 0),
                messageId = messageId,
                reactionClickListener = this
            )
            val emojiName = Reaction.emoji[emojiView.reaction]
            if (emojiName != null) {
                store.accept(
                    TopicEvent.Ui.AddReaction(
                        emojiView.messageId,
                        emojiName,
                        emojiView.reaction
                    )
                )
            }
        }
    }

    private fun initFileResult() =
        registerForActivityResult(ActivityResultContracts.GetContent()) { contentUri ->
            if (contentUri != null){
                val fileName = getFileName(this, contentUri)
                val file = File(cacheDir, fileName)
                file.createNewFile()
                try {
                    val outStream = FileOutputStream(file)
                    val inputStream = contentUri.let { contentResolver.openInputStream(it) }
                    inputStream?.let { Utils.copy(inputStream, outStream) }
                    outStream.flush()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val request: RequestBody = file
                    .asRequestBody(contentResolver.getType(contentUri)?.toMediaTypeOrNull())
                val body: MultipartBody.Part =
                    MultipartBody.Part.createFormData("file", file.name, request)
                store.accept(TopicEvent.Ui.UploadFile(fileName, body))
            }
        }

    companion object {
        const val CHANNEL_NAME = "channelName"
        const val TOPIC_NAME = "topicName"
        const val TOPIC_KEY = "topic"
        private const val SCROLL_POSITION_FOR_NEXT = 5
    }
}