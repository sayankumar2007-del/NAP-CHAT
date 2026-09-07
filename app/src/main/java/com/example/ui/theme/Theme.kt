package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SophisticatedDarkColorScheme = darkColorScheme(
  primary = SophisticatedPurple,
  onPrimary = SophisticatedOnPurple,
  primaryContainer = DarkInputBg,
  onPrimaryContainer = SophisticatedPurple,
  secondary = DiscordGreen,
  onSecondary = Color.White,
  tertiary = SophisticatedPeach,
  onTertiary = SophisticatedOnPeach,
  background = DarkCanvasBg,
  onBackground = TextBody,
  surface = DarkSidebarBg,
  onSurface = TextHeader,
  surfaceVariant = DarkCardBg,
  onSurfaceVariant = TextMuted,
  outline = BorderSubtle
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = SophisticatedDarkColorScheme,
    typography = Typography,
    content = content
  )
}


