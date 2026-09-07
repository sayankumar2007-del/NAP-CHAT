package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.ChatViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Discord Chat", appName)
  }

  @Test
  fun `chat viewmodel sends message and generates bot response`() {
    val viewModel = ChatViewModel()
    val initialMessageCount = viewModel.uiState.value.currentMessages.size

    viewModel.onInputChange("Hello world")
    viewModel.sendMessage()

    val state = viewModel.uiState.value
    assertTrue("Input text should be cleared after send", state.inputText.isEmpty())
    assertTrue("Message list should have at least 1 new message", state.currentMessages.size > initialMessageCount)
    assertEquals("Last sent message should match input", "Hello world", state.currentMessages.last().text)
  }

  @Test
  fun `chat viewmodel handles channel switching`() {
    val viewModel = ChatViewModel()
    viewModel.selectChannel("bot-testing")
    assertEquals("bot-testing", viewModel.uiState.value.activeChannelId)
  }

  @Test
  fun `chat viewmodel adds reaction`() {
    val viewModel = ChatViewModel()
    val firstMsg = viewModel.uiState.value.currentMessages.first()
    viewModel.addReaction(firstMsg.id, "🔥")

    val updatedMsg = viewModel.uiState.value.currentMessages.first { it.id == firstMsg.id }
    assertEquals(1, updatedMsg.reactions["🔥"])
  }
}

