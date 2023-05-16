package com.example.homework_2.ui

import androidx.test.core.app.ActivityScenario
import com.example.homework_2.MainActivity
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class StreamsTest : TestCase() {

    @Test
    fun initialization() = run {
        ActivityScenario.launch(MainActivity::class.java)

        step("TopicsList is visible") {
            SubscribedStream.streamsList.firstChild<SubscribedStream.StreamItem>{
                arrowIcon.click()
            }
            SubscribedStream.topicList.firstChild<SubscribedStream.TopicItem>{
                topic.click()
            }
        }

    }

    @Test
    fun clickOnTopic() = run {
        step("TopicsList") {
            SubscribedStream.streamsList.childAt<SubscribedStream.TopicItem>(1) {
                topicList {
                    isDisplayed()
                    var childCount = 0
                    children<SubscribedStream.TopicItem> { childCount++ }
                    // expected топиков может меняться, стоит поменять значение или мокать апи
                    Assert.assertEquals(20, childCount)

                }
            }
        }
    }
}



//    @Test
//    fun showSubscribedList() = run {
//        ActivityScenario.launch(MainActivity::class.java)
//
//        step("TopicsList is invisible") {
//            SubscribedStream.streamsList.children<SubscribedStream.StreamItem> {
//                topicsList {
//                    isNotDisplayed()
//                }
//            }
//        }
//        step("TopicsList is visible") {
//            SubscribedStream.streamsList.childAt<SubscribedStream.StreamItem>(0) {
//                arrowIcon.click()
//                topicsList {
//                    isDisplayed()
//                }
//            }
//        }
//        step("TopicsList is visible") {
//            SubscribedStream.streamsList.childAt<SubscribedStream.StreamItem>(1) {
//                topicsList {
//                    isDisplayed()
//                    var childCount = 0
//                    children<SubscribedStream.TopicItem> { childCount++ }
//                    // expected топиков может меняться, стоит поменять значение или мокать апи
//                    Assert.assertEquals(18, childCount)
//
//                }
//            }
//        }
//    }
