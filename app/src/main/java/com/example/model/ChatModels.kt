package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DiscordBlurple
import com.example.ui.theme.DiscordGreen
import com.example.ui.theme.DiscordYellow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class Server(
  val id: String,
  val name: String,
  val iconText: String,
  val channels: List<Channel>
)

data class Channel(
  val id: String,
  val name: String,
  val description: String = ""
)

data class ChatMessage(
  val id: String = UUID.randomUUID().toString(),
  val sender: String,
  val text: String,
  val timestamp: String = formatCurrentTime(),
  val avatarInitial: String = sender.firstOrNull()?.uppercase() ?: "?",
  val avatarColor: Color = if (sender == "You") DiscordGreen else DiscordBlurple,
  val isBot: Boolean = sender.contains("Bot", ignoreCase = true),
  val reactions: Map<String, Int> = emptyMap()
)

fun formatCurrentTime(): String {
  val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
  return "Today at ${sdf.format(Date())}"
}
