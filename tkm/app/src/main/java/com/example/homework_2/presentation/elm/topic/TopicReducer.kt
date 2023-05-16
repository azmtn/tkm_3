package com.example.homework_2.presentation.elm.topic

import com.example.homework_2.Utils.Companion.fromHexToDecimal
import com.example.homework_2.data.model.ReactionCounterItem
import com.example.homework_2.data.networking.ZulipApi.Companion.LAST_MESSAGES
import com.example.homework_2.domain.model.Topic
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

internal class TopicReducer : DslReducer<TopicEvent, TopicState, TopicEffect, TopicCommand>() {

    override fun Result.reduce(event: TopicEvent): Any {
        return when (event) {
            is TopicEvent.Ui.InitEvent -> {
                processInitEvent()
            }
            is TopicEvent.Ui.LoadLastMessages -> {
                processLoadLastMessagesEvent(event)
            }
            is TopicEvent.Ui.LoadPortionOfMessages -> {
                processLoadLastMessagesEvent(event)
            }
            is TopicEvent.Ui.SendMessage -> {
                processSendMessageEvent(event)
            }
            is TopicEvent.Ui.AddReaction -> {
                processAddReactionEvent(event)
            }
            is TopicEvent.Ui.RemoveReaction -> {
                processRemoveReactionEvent(event)
            }
            is TopicEvent.Ui.UploadFile -> {
                processFileUploadEvent(event)
            }
            is TopicEvent.Internal.LastMessagesLoaded -> {
                processLastMessagesLoadedEvent(event)
            }
            is TopicEvent.Internal.PortionOfMessagesLoaded -> {
                processPortionOfMessagesLoadedEvent(event)
            }
            is TopicEvent.Internal.MessageLoaded -> {
                processLoadMessageEvent(event)
            }
            is TopicEvent.Internal.MessageSent -> {
                processMessageSentEvent()
            }
            is TopicEvent.Internal.ReactionAdded -> {
                processReactionAddedEvent(event)
            }
            is TopicEvent.Internal.ReactionRemoved -> {
                processReactionRemovedEvent(event)
            }
            is TopicEvent.Internal.FileUploaded -> {
                processFileUploadedEvent(event)
            }
            is TopicEvent.Internal.MessagesLoadingError -> {
                processMessagesLoadingError(event)
            }
            is TopicEvent.Internal.MessageSendingError -> {
                processMessageSendingError(event)
            }
            is TopicEvent.Internal.FileUploadingError -> {
                processFileUploadingError(event)
            }
        }
    }

    private fun Result.processInitEvent() {
        state {
            copy(
                isLoading = true,
                error = null
            )
        }
    }

    private fun Result.processLoadLastMessagesEvent(event: TopicEvent.Ui.LoadLastMessages) {
        state {
            copy(
                isLoading = true,
                error = null,
                topicName = event.topicName
            )
        }
        commands {
            +TopicCommand.LoadLastMessages(
                topicName = event.topicName,
                anchor = event.anchor
            )
        }
    }

    private fun Result.processLoadLastMessagesEvent(event: TopicEvent.Ui.LoadPortionOfMessages) {
        state {
            copy(
                isLoading = true,
                error = null
            )
        }
        commands {
            +TopicCommand.LoadPortionOfMessages(
                topicName = event.topicName,
                anchor = event.anchor
            )
        }
    }

    private fun Result.processSendMessageEvent(event: TopicEvent.Ui.SendMessage) {
        commands {
            +TopicCommand.SendMessage(
                topicName = event.topicName,
                streamName = event.streamName,
                content = event.content
            )
        }
    }

    private fun Result.processAddReactionEvent(event: TopicEvent.Ui.AddReaction) {
        val itemsInState = state.items.toMutableList()
        val itemForUpdate = itemsInState.find { it.id == event.messageId }
        val emojiForUpdate = itemForUpdate?.emojis?.find {
            fromHexToDecimal(it.code) == event.emojiCode
        }
        if (emojiForUpdate != null) {
            emojiForUpdate.count = emojiForUpdate.count.plus(1)
            emojiForUpdate.selectedByCurrentUser = true
        } else {
            itemForUpdate?.emojis?.add(ReactionCounterItem(code = event.emojiCode, count = 1, selectedByCurrentUser = true))
        }
        state {
            copy(
                items = itemsInState,
                error = null
            )
        }
        commands {
            +TopicCommand.AddReaction(
                messageId = event.messageId,
                emojiName = event.emojiName
            )
        }
    }

    private fun Result.processRemoveReactionEvent(event: TopicEvent.Ui.RemoveReaction) {
        val itemsInState = state.items.toMutableList()
        val itemForUpdate = itemsInState.find { it.id == event.messageId }
        val emojiForUpdate = itemForUpdate?.emojis?.find {
            fromHexToDecimal(it.code) == event.emojiCode
        }
        if (emojiForUpdate != null && emojiForUpdate.count > 1) {
            emojiForUpdate.count = emojiForUpdate.count.minus(1)
            emojiForUpdate.selectedByCurrentUser = false
        } else {
            itemForUpdate?.emojis?.remove(emojiForUpdate)
        }
        state {
            copy(
                items = itemsInState,
                error = null
            )
        }
        commands {
            +TopicCommand.RemoveReaction(
                messageId = event.messageId,
                emojiName = event.emojiName
            )
        }
    }

    private fun Result.processLastMessagesLoadedEvent(
        event: TopicEvent.Internal.LastMessagesLoaded
    ) {
        state {
            copy(
                items = event.items,
                isLoading = false,
                error = null,
                anchor = if (event.items.isNotEmpty()) event.items[0].id - 1 else LAST_MESSAGES
            )
        }
    }

    private fun Result.processPortionOfMessagesLoadedEvent(
        event: TopicEvent.Internal.PortionOfMessagesLoaded
    ) {
        state {
            copy(
                items = event.items.plus(state.items),
                isLoading = false,
                error = null,
                anchor = if (event.items.isNotEmpty()) event.items[0].id - 1 else state.anchor
            )
        }
    }

    private fun Result.processLoadMessageEvent(
        event: TopicEvent.Internal.MessageLoaded
    ) {
        val itemsInState = state.items.toMutableList()
        val itemForUpdate = itemsInState.find { it.id == event.item.id }
        val indexForUpdate = itemsInState.indexOf(itemForUpdate)
        if (itemForUpdate != null && itemForUpdate != event.item) {
            itemsInState[indexForUpdate] = event.item
        }
        state {
            copy(
                isLoading = false,
                items = itemsInState
            )
        }
    }

    private fun Result.processMessageSentEvent() {
        state {
            copy(error = null)
        }
        commands { +TopicCommand.LoadLastMessages(
            topicName = state.topicName,
            anchor = LAST_MESSAGES,
            isFirstPosition = true
        ) }
        effects { +TopicEffect.MessageSentEffect }
    }

    private fun Result.processReactionAddedEvent(
        event: TopicEvent.Internal.ReactionAdded
    ) {
        state {
            copy(error = null)
        }
        commands {
            +TopicCommand.LoadMessage(
                messageId = event.messageId
            )
        }
    }

    private fun Result.processReactionRemovedEvent(
        event: TopicEvent.Internal.ReactionRemoved
    ) {
        state {
            copy(error = null)
        }
        commands {
            +TopicCommand.LoadMessage(
                messageId = event.messageId
            )
        }
    }

    private fun Result.processFileUploadEvent(
        event: TopicEvent.Ui.UploadFile
    ) {
        commands {
            +TopicCommand.UploadFile(
                event.fileName,
                event.fileBody
            )
        }
    }

    private fun Result.processMessagesLoadingError(
        event: TopicEvent.Internal.MessagesLoadingError
    ) {
        state { copy(error = event.error) }
        effects { +TopicEffect.MessagesLoadingError(event.error) }
    }

    private fun Result.processMessageSendingError(
        event: TopicEvent.Internal.MessageSendingError
    ) {
        state { copy(error = event.error) }
        effects { +TopicEffect.MessageSendingError(event.error) }
    }

    private fun Result.processFileUploadedEvent(
        event: TopicEvent.Internal.FileUploaded
    ) {
        state {
            copy(error = null)
        }
        effects { +TopicEffect.FileUploadedEffect(event.fileName, event.fileUri) }
    }

    private fun Result.processFileUploadingError(
        event: TopicEvent.Internal.FileUploadingError
    ) {
        state { copy(error = event.error) }
        effects {
            +TopicEffect.FileUploadingError(
                error = event.error,
                fileName = event.fileName,
                fileBody = event.fileBody
            )
        }
    }
}
