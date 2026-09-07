package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkInactiveServerBg
import com.example.ui.theme.DiscordBlurple
import com.example.ui.theme.SophisticatedOnPeach
import com.example.ui.theme.SophisticatedOnPurple
import com.example.ui.theme.SophisticatedPeach
import com.example.ui.theme.SophisticatedPurple
import com.example.ui.theme.TextBody
import com.example.ui.theme.TextHeader
import com.example.ui.theme.TextMuted

@Composable
fun ChatMessageItem(
  message: ChatMessage,
  onAddReaction: (emoji: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showReactionPicker by remember { mutableStateOf(false) }

  val avatarTextColor = when (message.avatarColor) {
    SophisticatedPeach -> SophisticatedOnPeach
    SophisticatedPurple -> SophisticatedOnPurple
    else -> Color.White
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .clickable { showReactionPicker = !showReactionPicker }
      .padding(horizontal = 8.dp, vertical = 6.dp)
      .testTag("message_item_${message.id}")
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.Top
    ) {
      // 40x40 Avatar
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(message.avatarColor),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = message.avatarInitial,
          color = avatarTextColor,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Message Content
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // Meta Row: Username, Bot Tag, Timestamp
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = message.sender,
            color = TextHeader,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
          )

          if (message.isBot) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(3.dp))
                .background(DiscordBlurple)
                .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
              Text(
                text = "BOT",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.2).sp
              )
            }
          }

          Text(
            text = message.timestamp,
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
          )
        }

        // Text Body
        if (message.isBot) {
          // Sophisticated Dark Bot Message Card
          Box(
            modifier = Modifier
              .clip(
                RoundedCornerShape(
                  topStart = 0.dp,
                  topEnd = 16.dp,
                  bottomEnd = 16.dp,
                  bottomStart = 16.dp
                )
              )
              .background(DarkCardBg)
              .border(1.dp, BorderSubtle, RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp))
              .padding(12.dp)
          ) {
            Text(
              text = highlightMentions(message.text),
              color = TextBody,
              fontSize = 14.sp,
              lineHeight = 20.sp
            )
          }
        } else {
          Text(
            text = message.text,
            color = TextBody,
            fontSize = 15.sp,
            lineHeight = 22.sp
          )
        }

        // Reaction chips
        if (message.reactions.isNotEmpty()) {
          Row(
            modifier = Modifier
              .padding(top = 4.dp)
              .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            message.reactions.forEach { (emoji, count) ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(DarkCardBg)
                  .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                  .clickable { onAddReaction(emoji) }
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Text(text = emoji, fontSize = 13.sp)
                  Text(text = "$count", color = TextHeader, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }

        // Quick Reaction Picker popup
        if (showReactionPicker) {
          Row(
            modifier = Modifier
              .padding(top = 6.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(DarkInactiveServerBg)
              .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            listOf("👍", "❤️", "😂", "🔥", "🚀").forEach { emoji ->
              Text(
                text = emoji,
                fontSize = 18.sp,
                modifier = Modifier
                  .clickable {
                    onAddReaction(emoji)
                    showReactionPicker = false
                  }
                  .padding(4.dp)
              )
            }
          }
        }
      }
    }
  }
}

private fun highlightMentions(text: String): androidx.compose.ui.text.AnnotatedString {
  val words = text.split(" ")
  return buildAnnotatedString {
    words.forEachIndexed { index, word ->
      if (word.startsWith("@") || word.startsWith("#")) {
        withStyle(style = SpanStyle(color = SophisticatedPurple, fontWeight = FontWeight.SemiBold)) {
          append(word)
        }
      } else {
        append(word)
      }
      if (index < words.size - 1) append(" ")
    }
  }
}

