package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.ui.components.ChannelSidebar
import com.example.ui.components.ChatInputBar
import com.example.ui.components.ChatMessageItem
import com.example.ui.components.ServerRail
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkCanvasBg
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkInactiveServerBg
import com.example.ui.theme.DarkSidebarBg
import com.example.ui.theme.DiscordGreen
import com.example.ui.theme.SophisticatedOnPeach
import com.example.ui.theme.SophisticatedOnPurple
import com.example.ui.theme.SophisticatedPeach
import com.example.ui.theme.SophisticatedPurple
import com.example.ui.theme.TextBody
import com.example.ui.theme.TextHeader
import com.example.ui.theme.TextMuted
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
  viewModel: ChatViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsState()
  val scope = rememberCoroutineScope()
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

  var showMembersDialog by remember { mutableStateOf(false) }

  BoxWithConstraints(modifier = modifier.fillMaxSize()) {
    val isTablet = maxWidth >= 600.dp

    if (isTablet) {
      // Expanded tablet/desktop layout: Server Rail + Channel Sidebar + Chat Area side-by-side
      Row(
        modifier = Modifier
          .fillMaxSize()
          .background(DarkCanvasBg)
          .statusBarsPadding()
      ) {
        ServerRail(
          servers = uiState.servers,
          activeServerId = uiState.activeServerId,
          onServerSelected = { viewModel.selectServer(it) },
          onAddServerClick = { /* Server creator */ }
        )

        VerticalDivider(thickness = 1.dp, color = BorderSubtle)

        ChannelSidebar(
          server = uiState.activeServer,
          activeChannelId = uiState.activeChannelId,
          onChannelSelected = { viewModel.selectChannel(it) },
          onAddChannel = { viewModel.addNewChannel(it) }
        )

        VerticalDivider(thickness = 1.dp, color = BorderSubtle)

        ChatMainContent(
          channelName = uiState.activeChannel.name,
          serverName = uiState.activeServer.name,
          messages = uiState.currentMessages,
          inputText = uiState.inputText,
          isBotTyping = uiState.isBotTyping,
          isTablet = true,
          onOpenDrawer = {},
          onOpenMembers = { showMembersDialog = true },
          onClearMessages = { viewModel.clearCurrentChannel() },
          onInputChange = { viewModel.onInputChange(it) },
          onSend = { viewModel.sendMessage() },
          onAddReaction = { msgId, emoji -> viewModel.addReaction(msgId, emoji) },
          modifier = Modifier.weight(1f)
        )
      }
    } else {
      // Phone layout with slide-over drawer containing Server Rail and Channel Sidebar
      ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
          ModalDrawerSheet(
            drawerContainerColor = DarkSidebarBg,
            modifier = Modifier.width(310.dp)
          ) {
            Row(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
              ServerRail(
                servers = uiState.servers,
                activeServerId = uiState.activeServerId,
                onServerSelected = {
                  viewModel.selectServer(it)
                },
                onAddServerClick = {}
              )

              VerticalDivider(thickness = 1.dp, color = BorderSubtle)

              ChannelSidebar(
                server = uiState.activeServer,
                activeChannelId = uiState.activeChannelId,
                onChannelSelected = {
                  viewModel.selectChannel(it)
                  scope.launch { drawerState.close() }
                },
                onAddChannel = {
                  viewModel.addNewChannel(it)
                  scope.launch { drawerState.close() }
                },
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      ) {
        ChatMainContent(
          channelName = uiState.activeChannel.name,
          serverName = uiState.activeServer.name,
          messages = uiState.currentMessages,
          inputText = uiState.inputText,
          isBotTyping = uiState.isBotTyping,
          isTablet = false,
          onOpenDrawer = { scope.launch { drawerState.open() } },
          onOpenMembers = { showMembersDialog = true },
          onClearMessages = { viewModel.clearCurrentChannel() },
          onInputChange = { viewModel.onInputChange(it) },
          onSend = { viewModel.sendMessage() },
          onAddReaction = { msgId, emoji -> viewModel.addReaction(msgId, emoji) },
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }

  // Members Dialog
  if (showMembersDialog) {
    AlertDialog(
      onDismissRequest = { showMembersDialog = false },
      containerColor = DarkCardBg,
      shape = RoundedCornerShape(20.dp),
      title = {
        Text("Channel Members", color = TextHeader, fontWeight = FontWeight.Bold, fontSize = 16.sp)
      },
      text = {
        Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
          Text("ONLINE — 2", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier.size(36.dp).clip(CircleShape).background(SophisticatedPurple),
              contentAlignment = Alignment.Center
            ) {
              Text("B", color = SophisticatedOnPurple, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Bot Assistant", color = TextHeader, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(SophisticatedPurple)
                    .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                  Text("BOT", color = SophisticatedOnPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
              }
              Text("Listening in #${uiState.activeChannel.name}", color = TextMuted, fontSize = 11.sp)
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier.size(36.dp).clip(CircleShape).background(SophisticatedPeach),
              contentAlignment = Alignment.Center
            ) {
              Text("A", color = SophisticatedOnPeach, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("Alex Chen (You)", color = TextHeader, fontSize = 14.sp, fontWeight = FontWeight.Medium)
              Text("Online", color = DiscordGreen, fontSize = 11.sp)
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text("OFFLINE — 2", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier.size(36.dp).clip(CircleShape).background(DarkInactiveServerBg),
              contentAlignment = Alignment.Center
            ) {
              Text("S", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text("Sarah Miller", color = TextMuted, fontSize = 14.sp)
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier.size(36.dp).clip(CircleShape).background(DarkInactiveServerBg),
              contentAlignment = Alignment.Center
            ) {
              Text("G", color = TextMuted, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text("Gamer_007", color = TextMuted, fontSize = 14.sp)
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showMembersDialog = false }) {
          Text("Close", color = SophisticatedPurple, fontWeight = FontWeight.Bold)
        }
      }
    )
  }
}

@Composable
private fun ChatMainContent(
  channelName: String,
  serverName: String,
  messages: List<ChatMessage>,
  inputText: String,
  isBotTyping: Boolean,
  isTablet: Boolean,
  onOpenDrawer: () -> Unit,
  onOpenMembers: () -> Unit,
  onClearMessages: () -> Unit,
  onInputChange: (String) -> Unit,
  onSend: () -> Unit,
  onAddReaction: (String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val listState = rememberLazyListState()

  // Scroll to bottom whenever new message is appended
  LaunchedEffect(messages.size, isBotTyping) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = DarkCanvasBg,
    topBar = {
      Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
        // Sophisticated Dark Header: h-14 (56dp), px-4, border-b border-white/5
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(DarkCanvasBg)
            .padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (!isTablet) {
            IconButton(
              onClick = onOpenDrawer,
              modifier = Modifier.size(36.dp).testTag("menu_drawer_button")
            ) {
              Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Open Channels Drawer",
                tint = TextHeader
              )
            }
            Spacer(modifier = Modifier.width(4.dp))
          }

          Text(
            text = "#",
            color = TextMuted,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
          )

          Spacer(modifier = Modifier.width(8.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = channelName,
              color = TextHeader,
              fontSize = 15.sp,
              fontWeight = FontWeight.SemiBold,
              letterSpacing = (-0.2).sp
            )
            if (!isTablet) {
              Text(
                text = serverName,
                color = TextMuted,
                fontSize = 10.sp
              )
            }
          }

          // Search / Inspect Icon
          IconButton(
            onClick = {},
            modifier = Modifier.size(36.dp).testTag("search_button")
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search Messages",
              tint = TextMuted,
              modifier = Modifier.size(20.dp)
            )
          }

          // Members Icon
          IconButton(
            onClick = onOpenMembers,
            modifier = Modifier.size(36.dp).testTag("members_button")
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = "Server Members",
              tint = TextMuted,
              modifier = Modifier.size(20.dp)
            )
          }

          // Clear Messages Action
          IconButton(
            onClick = onClearMessages,
            modifier = Modifier.size(36.dp).testTag("clear_chat_button")
          ) {
            Icon(
              imageVector = Icons.Default.ClearAll,
              contentDescription = "Clear Chat",
              tint = TextMuted,
              modifier = Modifier.size(20.dp)
            )
          }
        }
        HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
      }
    },
    bottomBar = {
      ChatInputBar(
        channelName = channelName,
        inputText = inputText,
        isBotTyping = isBotTyping,
        onInputChange = onInputChange,
        onSend = onSend
      )
    }
  ) { innerPadding ->
    LazyColumn(
      state = listState,
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 8.dp)
        .testTag("messages_list"),
      verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(12.dp))
        // Channel Welcome Header
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {
          Box(
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .background(DarkCardBg)
              .border(1.dp, BorderSubtle, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Tag,
              contentDescription = null,
              tint = SophisticatedPurple,
              modifier = Modifier.size(28.dp)
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Welcome to #$channelName!",
            color = TextHeader,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.3).sp
          )
          Text(
            text = "This is the start of the #$channelName channel.",
            color = TextMuted,
            fontSize = 13.sp
          )
        }
        HorizontalDivider(thickness = 1.dp, color = BorderSubtle, modifier = Modifier.padding(vertical = 8.dp))
      }

      items(messages, key = { it.id }) { message ->
        ChatMessageItem(
          message = message,
          onAddReaction = { emoji -> onAddReaction(message.id, emoji) }
        )
      }

      item {
        Spacer(modifier = Modifier.height(8.dp))
      }
    }
  }
}

