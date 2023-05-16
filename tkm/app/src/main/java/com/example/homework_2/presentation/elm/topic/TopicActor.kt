package com.example.homework_2.presentation.elm.topic

import com.example.homework_2.domain.interactor.TopicInteractor
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

internal class TopicActor(
    private val topicInteractor: TopicInteractor
) : ActorCompat<TopicCommand, TopicEvent> {

    override fun execute(command: TopicCommand): Observable<TopicEvent> = when (command) {
        is TopicCommand.LoadLastMessages ->
            topicInteractor.loadLastMessages(
                command.topicName,
                command.anchor
            )
                .mapEvents(
                    { messages ->
                        TopicEvent.Internal.LastMessagesLoaded(
                            items = messages,
                            topicName = command.topicName
                        )
                    },
                    { error -> TopicEvent.Internal.MessagesLoadingError(error) }
                )

        is TopicCommand.LoadPortionOfMessages ->
            topicInteractor.loadPortionOfMessages(
                command.topicName,
                command.anchor
            )
                .mapEvents(
                    { messages ->
                        TopicEvent.Internal.PortionOfMessagesLoaded(
                            items = messages,
                            topicName = command.topicName
                        )
                    },
                    { error -> TopicEvent.Internal.MessagesLoadingError(error) }
                )

        is TopicCommand.LoadMessage ->
            topicInteractor.loadMessage(command.messageId)
                .mapEvents(
                    { message -> TopicEvent.Internal.MessageLoaded(message) },
                    { error -> TopicEvent.Internal.MessagesLoadingError(error) }
                )

        is TopicCommand.SendMessage -> {
            topicInteractor.sendMessage(
                topic = command.topicName,
                stream = command.streamName,
                content = command.content
            )
                .mapEvents(
                    { TopicEvent.Internal.MessageSent },
                    { error -> TopicEvent.Internal.MessageSendingError(error) }
                )
        }

        is TopicCommand.AddReaction -> topicInteractor.addReaction(
            messageId = command.messageId,
            emojiName = command.emojiName
        )
            .mapSuccessEvent {
                TopicEvent.Internal.ReactionAdded(command.messageId)
            }

        is TopicCommand.RemoveReaction -> topicInteractor.removeReaction(
            messageId = command.messageId,
            emojiName = command.emojiName
        )
            .mapSuccessEvent {
                TopicEvent.Internal.ReactionRemoved(command.messageId)
            }

        is TopicCommand.UploadFile -> topicInteractor.uploadFile(fileBody = command.fileBody)
            .mapEvents(
                { response ->
                    TopicEvent.Internal.FileUploaded(
                        command.fileName,
                        response.uri
                    )
                },
                { error ->
                    TopicEvent.Internal.FileUploadingError(
                        error,
                        command.fileName,
                        command.fileBody
                    )
                }
            )
    }
}
