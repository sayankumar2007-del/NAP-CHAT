package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ButtonCircleBg
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkInputBg
import com.example.ui.theme.SophisticatedOnPurple
import com.example.ui.theme.SophisticatedPurple
import com.example.ui.theme.TextBody
import com.example.ui.theme.TextHeader
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPlaceholder

@Composable
fun ChatInputBar(
  channelName: String,
  inputText: String,
  isBotTyping: Boolean,
  onInputChange: (String) -> Unit,
  onSend: () -> Unit,
  modifier: Modifier = Modifier
) {
  val commands = listOf("/help", "/roll", "/ping", "/flip", "/quote")

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(Color.Transparent)
      .navigationBarsPadding()
      .imePadding()
      .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 12.dp)
  ) {
    // Typing indicator (matching Sophisticated Dark opacity & style)
    AnimatedVisibility(
      visible = isBotTyping,
      enter = fadeIn(),
      exit = fadeOut()
    ) {
      Row(
        modifier = Modifier
          .padding(bottom = 6.dp, start = 4.dp)
          .alpha(0.7f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(SophisticatedPurple)
        )
        Text(
          text = "Bot Assistant is typing...",
          color = TextMuted,
          fontSize = 13.sp,
          fontStyle = FontStyle.Italic
        )
      }
    }

    // Quick Command Pills
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(commands) { cmd ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCardBg)
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .clickable {
              onInputChange(cmd)
              onSend()
            }
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .testTag("command_chip_$cmd")
        ) {
          Text(
            text = cmd,
            color = SophisticatedPurple,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    // Input Box Container (Sophisticated Dark: bg-[#383A40], rounded-2xl, min-h-[52px], border border-white/5)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 52.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(DarkInputBg)
        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
        .padding(horizontal = 12.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Plus Icon (Sophisticated Dark: w-8 h-8 bg-white/10 rounded-full flex items-center justify-center)
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(ButtonCircleBg)
          .clickable { onInputChange("Hey everyone! 👋") }
          .testTag("attachment_button"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "+",
          color = TextHeader,
          fontSize = 18.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Text Input
      Box(
        modifier = Modifier
          .weight(1f)
          .padding(vertical = 6.dp)
      ) {
        if (inputText.isEmpty()) {
          Text(
            text = "Message #$channelName",
            color = TextPlaceholder,
            fontSize = 14.sp
          )
        }
        BasicTextField(
          value = inputText,
          onValueChange = onInputChange,
          textStyle = TextStyle(
            color = TextBody,
            fontSize = 14.sp
          ),
          cursorBrush = SolidColor(SophisticatedPurple),
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
          keyboardActions = KeyboardActions(onSend = { onSend() }),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("message_input")
        )
      }

      // Send Button
      IconButton(
        onClick = onSend,
        enabled = inputText.isNotBlank(),
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(if (inputText.isNotBlank()) SophisticatedPurple else Color.Transparent)
          .testTag("send_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.Send,
          contentDescription = "Send Message",
          tint = if (inputText.isNotBlank()) SophisticatedOnPurple else TextMuted,
          modifier = Modifier.size(17.dp)
        )
      }
    }
  }
}

