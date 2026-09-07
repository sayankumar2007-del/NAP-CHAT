package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Server
import com.example.ui.theme.BorderDivider
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkAddServerBg
import com.example.ui.theme.DarkCanvasBg
import com.example.ui.theme.DarkInactiveServerBg
import com.example.ui.theme.DarkNavBg
import com.example.ui.theme.SophisticatedOnPurple
import com.example.ui.theme.SophisticatedPurple
import com.example.ui.theme.TextHeader
import com.example.ui.theme.TextMuted

@Composable
fun ServerRail(
  servers: List<Server>,
  activeServerId: String,
  onServerSelected: (String) -> Unit,
  onAddServerClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .width(68.dp)
      .fillMaxHeight()
      .background(DarkNavBg)
      .padding(vertical = 16.dp)
      .verticalScroll(rememberScrollState())
      .testTag("server_rail"),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    servers.forEach { server ->
      val isActive = server.id == activeServerId
      ServerIconItem(
        server = server,
        isActive = isActive,
        onClick = { onServerSelected(server.id) }
      )
    }

    HorizontalDivider(
      modifier = Modifier
        .width(32.dp)
        .padding(vertical = 4.dp),
      thickness = 1.dp,
      color = BorderDivider
    )

    // Add Server Action Button (Sophisticated Dark styled)
    Box(
      modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .background(DarkAddServerBg)
        .border(2.dp, DarkCanvasBg, CircleShape)
        .clickable(onClick = onAddServerClick)
        .testTag("add_server_button"),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "+",
        color = TextHeader,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )
    }

    // Explore / Discovery Icon
    Box(
      modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .background(DarkInactiveServerBg)
        .clickable(onClick = {})
        .testTag("explore_servers_button"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Explore,
        contentDescription = "Explore Public Servers",
        tint = TextMuted,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}

@Composable
private fun ServerIconItem(
  server: Server,
  isActive: Boolean,
  onClick: () -> Unit
) {
  val cornerRadius by animateDpAsState(
    targetValue = if (isActive) 16.dp else 22.dp,
    animationSpec = tween(durationMillis = 200),
    label = "cornerRadius"
  )

  val bgColor by animateColorAsState(
    targetValue = if (isActive) SophisticatedPurple else DarkInactiveServerBg,
    animationSpec = tween(durationMillis = 200),
    label = "bgColor"
  )

  val pillHeight by animateDpAsState(
    targetValue = if (isActive) 38.dp else 0.dp,
    animationSpec = tween(durationMillis = 200),
    label = "pillHeight"
  )

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(48.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Indicator Pill on the left
    Box(
      modifier = Modifier
        .width(4.dp)
        .height(pillHeight)
        .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
        .background(if (isActive) SophisticatedPurple else Color.White)
    )

    Spacer(modifier = Modifier.width(if (isActive) 6.dp else 10.dp))

    // Server Avatar Button
    val shape = RoundedCornerShape(cornerRadius)
    Box(
      modifier = Modifier
        .size(48.dp)
        .then(
          if (isActive) {
            Modifier.shadow(
              elevation = 6.dp,
              shape = shape,
              ambientColor = SophisticatedPurple.copy(alpha = 0.2f),
              spotColor = SophisticatedPurple.copy(alpha = 0.2f)
            )
          } else Modifier
        )
        .clip(shape)
        .background(bgColor)
        .clickable(onClick = onClick)
        .testTag("server_item_${server.id}"),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = server.iconText,
        color = if (isActive) SophisticatedOnPurple else TextHeader,
        fontSize = if (server.iconText.length > 1) 20.sp else 19.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

