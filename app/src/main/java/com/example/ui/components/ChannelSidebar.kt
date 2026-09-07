package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Channel
import com.example.model.Server
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkInactiveServerBg
import com.example.ui.theme.DarkInputBg
import com.example.ui.theme.DarkSidebarBg
import com.example.ui.theme.DiscordGreen
import com.example.ui.theme.SophisticatedOnPeach
import com.example.ui.theme.SophisticatedPeach
import com.example.ui.theme.SophisticatedPurple
import com.example.ui.theme.TextHeader
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextBody

@Composable
fun ChannelSidebar(
  server: Server,
  activeChannelId: String,
  onChannelSelected: (String) -> Unit,
  onAddChannel: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showAddChannelDialog by remember { mutableStateOf(false) }
  var newChannelName by remember { mutableStateOf("") }

  Column(
    modifier = modifier
      .width(230.dp)
      .fillMaxHeight()
      .background(DarkSidebarBg)
  ) {
    // Sidebar Header: Server Name (Sophisticated Dark 56dp height)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = server.name,
        color = TextHeader,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.2).sp
      )
    }

    HorizontalDivider(thickness = 1.dp, color = BorderSubtle)

    // Channel List
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 10.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      // Category Header: TEXT CHANNELS
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "TEXT CHANNELS",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.8.sp
        )
        IconButton(
          onClick = { showAddChannelDialog = true },
          modifier = Modifier.size(22.dp).testTag("add_channel_icon")
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Create Channel",
            tint = TextMuted,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      // Channels
      server.channels.forEach { channel ->
        val isActive = channel.id == activeChannelId
        ChannelListItem(
          channel = channel,
          isActive = isActive,
          onClick = { onChannelSelected(channel.id) }
        )
      }
    }

    HorizontalDivider(thickness = 1.dp, color = BorderSubtle)

    // User Profile Bar at bottom
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .background(DarkSidebarBg)
        .padding(horizontal = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(modifier = Modifier.size(36.dp)) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(SophisticatedPeach),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Y",
            color = SophisticatedOnPeach,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        }
        // Online status green dot
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(DiscordGreen)
            .border(2.dp, DarkSidebarBg, CircleShape)
            .align(Alignment.BottomEnd)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "You",
          color = TextHeader,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = "Online",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Icon(
        imageVector = Icons.Default.Mic,
        contentDescription = "Mute",
        tint = TextMuted,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Icon(
        imageVector = Icons.Default.Headphones,
        contentDescription = "Deafen",
        tint = TextMuted,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Icon(
        imageVector = Icons.Default.Settings,
        contentDescription = "Settings",
        tint = TextMuted,
        modifier = Modifier.size(18.dp)
      )
    }
  }

  // Create Channel Dialog
  if (showAddChannelDialog) {
    AlertDialog(
      onDismissRequest = {
        showAddChannelDialog = false
        newChannelName = ""
      },
      containerColor = DarkCardBg,
      title = {
        Text("Create Text Channel", color = TextHeader, fontSize = 16.sp, fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("CHANNEL NAME", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
          OutlinedTextField(
            value = newChannelName,
            onValueChange = { newChannelName = it },
            placeholder = { Text("new-channel", color = TextMuted) },
            leadingIcon = {
              Icon(Icons.Default.Numbers, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
            },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = TextBody,
              unfocusedTextColor = TextBody,
              focusedContainerColor = DarkInputBg,
              unfocusedContainerColor = DarkInputBg,
              focusedBorderColor = SophisticatedPurple,
              unfocusedBorderColor = BorderSubtle
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("new_channel_input")
          )
        }
      },
      confirmButton = {
        TextButton(
          onClick = {
            if (newChannelName.isNotBlank()) {
              onAddChannel(newChannelName)
              showAddChannelDialog = false
              newChannelName = ""
            }
          },
          modifier = Modifier.testTag("confirm_create_channel")
        ) {
          Text("Create", color = SophisticatedPurple, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = {
          showAddChannelDialog = false
          newChannelName = ""
        }) {
          Text("Cancel", color = TextMuted)
        }
      }
    )
  }
}

@Composable
private fun ChannelListItem(
  channel: Channel,
  isActive: Boolean,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(if (isActive) DarkInactiveServerBg else Color.Transparent)
      .then(
        if (isActive) Modifier.border(1.dp, BorderSubtle, RoundedCornerShape(8.dp)) else Modifier
      )
      .clickable(onClick = onClick)
      .padding(horizontal = 10.dp, vertical = 8.dp)
      .testTag("channel_item_${channel.name}"),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "#",
      color = if (isActive) TextHeader else TextMuted,
      fontSize = 16.sp,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(end = 8.dp)
    )
    Text(
      text = channel.name,
      color = if (isActive) TextHeader else TextMuted,
      fontSize = 14.sp,
      fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
    )
  }
}

