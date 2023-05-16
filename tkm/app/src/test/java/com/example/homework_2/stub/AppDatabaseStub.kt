package com.example.homework_2.stub

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.homework_2.data.db.AppDatabase
import com.example.homework_2.data.db.dao.MessageDao
import com.example.homework_2.data.db.dao.StreamDao
import com.example.homework_2.data.db.dao.SubscribedDao
import com.example.homework_2.data.db.dao.UserDao
import com.example.homework_2.data.db.model.StreamDb
import com.example.homework_2.data.model.TopicItem
import io.reactivex.Single
import org.mockito.Mockito
import org.mockito.Mockito.anyList
import org.mockito.Mockito.mock

internal class AppDatabaseStub : AppDatabase() {

    override fun userDao(): UserDao {
        return mock(UserDao::class.java)
    }

    override fun messageDao(): MessageDao {
        return mock(MessageDao::class.java)
    }

    override fun streamDao(): StreamDao {
        val streamDaoMock = mock(StreamDao::class.java)
        mockStreamDaoGet(streamDaoMock)
        mockStreamDaoSave(streamDaoMock)
        return streamDaoMock
    }

    override fun subscribedDao(): SubscribedDao {
        return mock(SubscribedDao::class.java)
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return mock(InvalidationTracker::class.java)
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        return mock(SupportSQLiteOpenHelper::class.java)
    }

    override fun clearAllTables() { }

    private fun mockStreamDaoSave(streamDaoMock: StreamDao) {
        Mockito.`when`(streamDaoMock.saveStream(anyList())).thenReturn(Single.just(emptyList()))
    }

    private fun mockStreamDaoGet(streamDaoMock: StreamDao) {
        Mockito.`when`(streamDaoMock.getStream()).thenReturn(
            Single.just(
                listOf(
                    StreamDb(
                        streamId = 1L,
                        name = "stream_1",
                        topics = listOf(
                            TopicItem("topic_1"),
                            TopicItem("topic_4"),
                            )
                    ),
                    StreamDb(
                        streamId = 2L,
                        name = "stream_2",
                        topics = listOf(
                            TopicItem("topic_2"),
                            TopicItem("topic_3")
                        )
                    )
                )
            )
        )
    }

}
