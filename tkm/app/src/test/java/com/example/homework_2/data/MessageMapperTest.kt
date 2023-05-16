package com.example.homework_2.data

import com.example.homework_2.data.db.model.MessageDb
import com.example.homework_2.data.db.model.ReactionCounterItemDbModel
import com.example.homework_2.data.mapper.MessageDbMapper.toModel
import com.example.homework_2.data.mapper.MessageDbMapper.toModelList
import com.example.homework_2.data.mapper.MessageItemMapper.toModel
import com.example.homework_2.data.mapper.MessageMapper.toModel
import com.example.homework_2.data.model.MessageItem
import com.example.homework_2.data.model.Reaction
import com.example.homework_2.data.model.ReactionCounterItem
import com.example.homework_2.domain.model.Message
import org.junit.Test
import org.junit.Assert.assertEquals


internal class MessageMapperTest {

    @Test
    fun `messageDb toMessageDto returns message`() {
        val messageDb = createMessageDb(
            id = 1L,
            userId = 1L,
            userFullName = "User_1",
            topicName = "topic_1",
            avatarUrl = "https://testUrl",
            content = "test",
            emojis = getReactionDbTestList(),
            timestamp = 10029385934L
        )

        val message = messageDb.toModel()

        assertEquals(1L, message.id)
        assertEquals(1L, message.userId)
        assertEquals("User_1", message.userFullName)
        assertEquals("topic_1", message.topicName)
        assertEquals("https://testUrl", message.avatarUrl)
        assertEquals("test", message.content)
        assertEquals(getReactionsTestList(), message.emojis)
        assertEquals(10029385934L, message.timestamp)
    }

    @Test
    fun `messagesDb toMessagesDto returns messages list`() {
        val messagesDb = listOf(
            createMessageDb(
                id = 1L,
                userId = 1L,
                userFullName = "User_1",
                topicName = "topic_1",
                avatarUrl = "https://testUrl",
                content = "test",
                emojis = getReactionDbTestList(),
                timestamp = 10029385934L
            ),
            createMessageDb(
                id = 2L,
                userId = 2L,
                userFullName = "User_2",
                topicName = "topic_2",
                avatarUrl = "https://secondTestUrl",
                content = "test_2",
                emojis = getReactionDbTestList(),
                timestamp = 1650034698L
            )
        )

        val messages = messagesDb.toModelList()

        assertEquals(1L, messages[0].id)
        assertEquals(1L, messages[0].userId)
        assertEquals("User_1", messages[0].userFullName)
        assertEquals("topic_1", messages[0].topicName)
        assertEquals("https://testUrl", messages[0].avatarUrl)
        assertEquals("test", messages[0].content)
        assertEquals(getReactionsTestList(), messages[0].emojis)
        assertEquals(10029385934L, messages[0].timestamp)

        assertEquals(2L, messages[1].id)
        assertEquals(2L, messages[1].userId)
        assertEquals("User_2", messages[1].userFullName)
        assertEquals("topic_2", messages[1].topicName)
        assertEquals("https://secondTestUrl", messages[1].avatarUrl)
        assertEquals("test_2", messages[1].content)
        assertEquals(getReactionsTestList(), messages[1].emojis)
        assertEquals(1650034698L, messages[1].timestamp)
    }

    @Test
    fun `MessageItem toMessage returns message`() {
        val messageItem = createMessageItem(
            id = 1L,
            userId = 1L,
            userFullName = "User_1",
            topicName = "topic_1",
            avatarUrl = "https://testUrl",
            content = "test",
            emojis = getReactionTestList(),
            timestamp = 10029385934L

        )

        val message = messageItem.toModel()

        assertEquals(1L, message.id)
        assertEquals(1L, message.userId)
        assertEquals("User_1", message.userFullName)
        assertEquals("topic_1", message.topicName)
        assertEquals("https://testUrl", message.avatarUrl)
        assertEquals("test", message.content)
        assertEquals(getReactionsTestList(), message.emojis)
        assertEquals(10029385934L, message.timestamp)
    }

    @Test
    fun `message toMessageDb returns message`() {
        val message = createMessage(
            id = 1L,
            userId = 1L,
            userFullName = "User_1",
            topicName = "topic_1",
            avatarUrl = "https://testUrl",
            content = "test",
            emojis = getReactionsTestList(),
            timestamp = 10029385934L
        )

        val messageDb = message.toModel()

        assertEquals(1L, messageDb.id)
        assertEquals(1L, messageDb.userId)
        assertEquals("User_1", messageDb.userFullName)
        assertEquals("topic_1", messageDb.topicName)
        assertEquals("https://testUrl", messageDb.avatarUrl)
        assertEquals("test", messageDb.content)
        assertEquals(getReactionDbTestList(), messageDb.emojis)
        assertEquals(10029385934L, messageDb.timestamp)
    }

    private fun createMessage(
        id: Long,
        userId: Long,
        userFullName: String,
        topicName: String,
        avatarUrl: String,
        content: String,
        emojis: List<ReactionCounterItem>,
        timestamp: Long
    ): Message {
        return Message(
            id = id,
            userId = userId,
            userFullName = userFullName,
            topicName = topicName,
            avatarUrl = avatarUrl,
            content = content,
            emojis = emojis.toMutableList(),
            timestamp = timestamp
        )
    }

    private fun createMessageItem(
        id: Long,
        userId: Long,
        userFullName: String,
        topicName: String,
        avatarUrl: String,
        content: String,
        emojis: List<Reaction>,
        timestamp: Long
    ): MessageItem {
        return MessageItem(
            id = id,
            userId = userId,
            userFullName = userFullName,
            topicName = topicName,
            avatarUrl = avatarUrl,
            content = content,
            reactions = emojis,
            timestamp = timestamp
        )
    }

    private fun createMessageDb(
        id: Long = 0,
        userId: Long,
        userFullName: String,
        topicName: String,
        avatarUrl: String?,
        content: String,
        emojis: List<ReactionCounterItemDbModel>,
        timestamp: Long
    ): MessageDb {
        return MessageDb(
            id = id,
            userId = userId,
            userFullName = userFullName,
            topicName = topicName,
            avatarUrl = avatarUrl,
            content = content,
            emojis = emojis,
            timestamp = timestamp
        )
    }

    private fun getReactionDbTestList(): List<ReactionCounterItemDbModel> {
        return listOf(
            ReactionCounterItemDbModel(
                code = "\uD83E\uDDE0",
                count = 1,
                selectedByCurrentUser = false
            ),
            ReactionCounterItemDbModel(
                code = "\uD83D\uDE04",
                count = 2,
                selectedByCurrentUser = false
            )
        )
    }

    private fun getReactionsTestList(): List<ReactionCounterItem> {
        return listOf(
            ReactionCounterItem(
                code = "\uD83E\uDDE0",
                count = 1,
                selectedByCurrentUser = false
            ),
            ReactionCounterItem(
                code = "\uD83D\uDE04",
                count = 2,
                selectedByCurrentUser = false
            )
        )
    }

    private fun getReactionTestList(): List<Reaction> {
        return listOf(
            Reaction(
                userId = 1L,
                emojiName = "reaction_1",
                emojiCode = "\uD83E\uDDE0"
            ),
            Reaction(
                userId = 1L,
                emojiName = "reaction_2",
                emojiCode = "\uD83D\uDE04"
            ),
            Reaction(
                userId = 1L,
                emojiName = "reaction_3",
                emojiCode = "\uD83D\uDE04"
            )
        )
    }
}
