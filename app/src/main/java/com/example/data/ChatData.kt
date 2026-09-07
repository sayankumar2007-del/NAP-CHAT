package com.example.data

import com.example.model.Channel
import com.example.model.ChatMessage
import com.example.model.Server
import com.example.ui.theme.SophisticatedPeach
import com.example.ui.theme.SophisticatedPurple
import kotlin.random.Random

object ChatData {
  val defaultServers = listOf(
    Server(
      id = "server_my",
      name = "My Server",
      iconText = "D",
      channels = listOf(
        Channel(id = "general", name = "general", description = "General chat and hangout"),
        Channel(id = "bot-testing", name = "bot-testing", description = "Test bot commands and scripts"),
        Channel(id = "game-room", name = "game-room", description = "Find players and organize games")
      )
    ),
    Server(
      id = "server_gaming",
      name = "Gaming Lounge",
      iconText = "🎮",
      channels = listOf(
        Channel(id = "lobby", name = "lobby", description = "Casual talk about new releases"),
        Channel(id = "squad-up", name = "squad-up", description = "LFG for multiplayer sessions"),
        Channel(id = "highlights", name = "highlights", description = "Clips, victories and epic plays")
      )
    ),
    Server(
      id = "server_ai",
      name = "Bot Sandbox",
      iconText = "🤖",
      channels = listOf(
        Channel(id = "playground", name = "playground", description = "Interactive bot sandbox"),
        Channel(id = "announcements", name = "announcements", description = "Bot updates and release notes")
      )
    )
  )

  fun getInitialMessages(): Map<String, List<ChatMessage>> {
    return mapOf(
      "general" to listOf(
        ChatMessage(
          id = "msg_init_1",
          sender = "Alex Chen",
          text = "Hey everyone! Has anyone had a chance to look at the new design system specs? The Material 3 updates look solid.",
          timestamp = "12:04 PM",
          avatarInitial = "A",
          avatarColor = SophisticatedPeach,
          isBot = false
        ),
        ChatMessage(
          id = "msg_init_2",
          sender = "Bot Assistant",
          text = "Welcome @Alex! I've pinned the latest documentation in the #resources channel for you.",
          timestamp = "12:05 PM",
          avatarInitial = "B",
          avatarColor = SophisticatedPurple,
          isBot = true
        )
      ),
      "bot-testing" to listOf(
        ChatMessage(
          id = "msg_init_3",
          sender = "Bot Assistant",
          text = "🤖 Bot testing sandbox ready! You can try commands like /help, /roll, /ping, /flip, or /quote.",
          timestamp = "11:30 AM",
          avatarInitial = "B",
          avatarColor = SophisticatedPurple,
          isBot = true
        )
      ),
      "game-room" to listOf(
        ChatMessage(
          id = "msg_init_4",
          sender = "Bot Assistant",
          text = "🎮 Welcome gamers! Who is ready for a match tonight?",
          timestamp = "10:15 AM",
          avatarInitial = "B",
          avatarColor = SophisticatedPurple,
          isBot = true
        )
      ),
      "lobby" to listOf(
        ChatMessage(
          id = "msg_init_5",
          sender = "Bot Assistant",
          text = "Welcome to Gaming Lounge! Share your gamertag or favorite titles.",
          timestamp = "09:45 AM",
          avatarInitial = "B",
          avatarColor = SophisticatedPurple,
          isBot = true
        )
      ),
      "playground" to listOf(
        ChatMessage(
          id = "msg_init_6",
          sender = "Bot Assistant",
          text = "🤖 AI Bot Playground online. Type anything or ask a question!",
          timestamp = "08:00 AM",
          avatarInitial = "B",
          avatarColor = SophisticatedPurple,
          isBot = true
        )
      )
    )
  }

  fun generateBotReply(userMessage: String, channelName: String): String {
    val trimmed = userMessage.trim()
    val lower = trimmed.lowercase()

    return when {
      lower == "/help" -> {
        "Available bot commands:\n• /ping - Check latency\n• /roll - Roll a 1-100 die\n• /flip - Flip a coin\n• /quote - Random inspirational quote\n• /shrug - ¯\\_(ツ)_/¯\n• /tableflip - (╯°□°)╯︵ ┻━┻"
      }
      lower == "/ping" -> {
        val ping = Random.nextInt(18, 48)
        "🏓 Pong! Latency: ${ping}ms. All systems operational."
      }
      lower == "/roll" -> {
        val roll = Random.nextInt(1, 101)
        "🎲 You rolled a $roll! ${if (roll >= 90) "Critical success! 🔥" else if (roll <= 10) "Critical fumble! 💀" else ""}"
      }
      lower == "/flip" -> {
        val coin = if (Random.nextBoolean()) "Heads! 🪙" else "Tails! 🪙"
        "The coin spun through the air and landed on: **$coin**"
      }
      lower == "/quote" -> {
        val quotes = listOf(
          "\"The future belongs to those who prepare for it today.\" - Malcolm X",
          "\"Keep your eyes on the stars, and your feet on the ground.\" - Theodore Roosevelt",
          "\"It always seems impossible until it's done.\" - Nelson Mandela",
          "\"GL HF! Good luck, have fun!\" - Gamers worldwide"
        )
        quotes.random()
      }
      lower == "/shrug" -> "¯\\_(ツ)_/¯"
      lower == "/tableflip" -> "(╯°□°)╯︵ ┻━┻"
      channelName == "bot-testing" -> {
        "Got your message! Try typing /help to see test commands, or /roll to test the RNG!"
      }
      channelName == "game-room" -> {
        "Awesome! Added that note to the lobby board. Game on! 🎮"
      }
      lower.contains("hello") || lower.contains("hi") || lower.contains("hey") -> {
        "Hey there! Great to see you in #$channelName! 👋"
      }
      else -> {
        "Got your message! Feel free to keep chatting in #$channelName."
      }
    }
  }
}

