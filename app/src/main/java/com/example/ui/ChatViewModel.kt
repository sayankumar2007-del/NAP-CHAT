package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ChatData
import com.example.model.Channel
import com.example.model.ChatMessage
import com.example.model.Server
import com.example.ui.theme.DiscordBlurple
import com.example.ui.theme.DiscordGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
  val servers: List<Server> = ChatData.defaultServers,
  val activeServerId: String = ChatData.defaultServers.first().id,
  val activeChannelId: String = ChatData.defaultServers.first().channels.first().id,
  val messagesByChannel: Map<String, List<ChatMessage>> = ChatData.getInitialMessages(),
  val inputText: String = "",
  val isBotTyping: Boolean = false,
  val isMemberDrawerOpen: Boolean = false
) {
  val activeServer: Server
    get() = servers.find { it.id == activeServerId } ?: servers.first()

  val activeChannel: Channel
    get() = activeServer.channels.find { it.id == activeChannelId }
      ?: activeServer.channels.firstOrNull()
      ?: Channel("general", "general")

  val currentMessages: List<ChatMessage>
    get() = messagesByChannel[activeChannelId] ?: emptyList()
}

class ChatViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(ChatUiState())
  val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

  fun selectServer(serverId: String) {
    _uiState.update { state ->
      val server = state.servers.find { it.id == serverId } ?: return@update state
      val firstChannelId = server.channels.firstOrNull()?.id ?: "general"
      state.copy(
        activeServerId = serverId,
        activeChannelId = firstChannelId
      )
    }
  }

  fun selectChannel(channelId: String) {
    _uiState.update { it.copy(activeChannelId = channelId) }
  }

  fun onInputChange(newText: String) {
    _uiState.update { it.copy(inputText = newText) }
  }

  fun sendMessage(messageText: String = _uiState.value.inputText) {
    val trimmed = messageText.trim()
    if (trimmed.isEmpty()) return

    val currentChannelId = _uiState.value.activeChannelId
    val currentChannelName = _uiState.value.activeChannel.name

    val userMessage = ChatMessage(
      sender = "You",
      text = trimmed,
      avatarInitial = "Y",
      avatarColor = DiscordGreen,
      isBot = false
    )

    _uiState.update { state ->
      val updatedList = (state.messagesByChannel[currentChannelId] ?: emptyList()) + userMessage
      state.copy(
        inputText = "",
        messagesByChannel = state.messagesByChannel + (currentChannelId to updatedList),
        isBotTyping = true
      )
    }

    // Simulate bot reply after ~700ms (as in script.js)
    viewModelScope.launch {
      delay(700)
      val botReplyText = ChatData.generateBotReply(trimmed, currentChannelName)
      val botMessage = ChatMessage(
        sender = "Bot Assistant",
        text = botReplyText,
        avatarInitial = "B",
        avatarColor = DiscordBlurple,
        isBot = true
      )

      _uiState.update { state ->
        val updatedList = (state.messagesByChannel[currentChannelId] ?: emptyList()) + botMessage
        state.copy(
          messagesByChannel = state.messagesByChannel + (currentChannelId to updatedList),
          isBotTyping = false
        )
      }
    }
  }

  fun addReaction(messageId: String, emoji: String) {
    val currentChannelId = _uiState.value.activeChannelId
    _uiState.update { state ->
      val messages = state.messagesByChannel[currentChannelId] ?: return@update state
      val updated = messages.map { msg ->
        if (msg.id == messageId) {
          val currentCount = msg.reactions[emoji] ?: 0
          val updatedReactions = if (currentCount > 0) {
            msg.reactions + (emoji to (currentCount + 1))
          } else {
            msg.reactions + (emoji to 1)
          }
          msg.copy(reactions = updatedReactions)
        } else {
          msg
        }
      }
      state.copy(messagesByChannel = state.messagesByChannel + (currentChannelId to updated))
    }
  }

  fun clearCurrentChannel() {
    val currentChannelId = _uiState.value.activeChannelId
    _uiState.update { state ->
      val welcome = ChatMessage(
        sender = "Bot Assistant",
        text = "Channel messages cleared. Welcome back!",
        avatarInitial = "B",
        avatarColor = DiscordBlurple,
        isBot = true
      )
      state.copy(messagesByChannel = state.messagesByChannel + (currentChannelId to listOf(welcome)))
    }
  }

  fun addNewChannel(channelName: String) {
    val cleanName = channelName.trim().lowercase().replace("\\s+".toRegex(), "-")
    if (cleanName.isEmpty()) return

    _uiState.update { state ->
      val activeServer = state.activeServer
      if (activeServer.channels.any { it.name == cleanName }) return@update state

      val newChannel = Channel(
        id = cleanName,
        name = cleanName,
        description = "User created channel #$cleanName"
      )
      val updatedChannels = activeServer.channels + newChannel
      val updatedServers = state.servers.map {
        if (it.id == activeServer.id) it.copy(channels = updatedChannels) else it
      }

      val welcome = ChatMessage(
        sender = "Bot Assistant",
        text = "Welcome to the new channel #$cleanName! Start the conversation.",
        avatarInitial = "B",
        avatarColor = DiscordBlurple,
        isBot = true
      )

      state.copy(
        servers = updatedServers,
        activeChannelId = cleanName,
        messagesByChannel = state.messagesByChannel + (cleanName to listOf(welcome))
      )
    }
  }
}
