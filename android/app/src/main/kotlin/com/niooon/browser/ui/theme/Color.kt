package com.niooon.browser.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val LiquidBlueDeep = Color(0xFF64B5F6)
val LiquidBlueMid = Color(0xFF90CAF9)
val LiquidBlueLight = Color(0xFFE3F2FD)
val LiquidCyanLight = Color(0xFFE0F7FA)

val GlassWhiteHigh = Color(0xCCFFFFFF) // ~80% white
val GlassWhiteMedium = Color(0x99FFFFFF) // ~60% white
val GlassWhiteLow = Color(0x59FFFFFF) // ~35% white
val GlassBorderWhite = Color(0x80FFFFFF) // ~50% white
val GlassHighlight = Color(0xE6FFFFFF) // ~90% white

val GoogleBlue = Color(0xFF4285F4)
val GoogleRed = Color(0xFFEA4335)
val GoogleYellow = Color(0xFFFBBC05)
val GoogleGreen = Color(0xFF34A853)

val TextPrimary = Color(0xFF1E293B)
val TextSecondary = Color(0xFF64748B)
val TextMuted = Color(0xFF94A3B8)

val LiquidBackgroundBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFBBDEFB),
        Color(0xFF90CAF9),
        Color(0xFFB3E5FC),
        Color(0xFFE1F5FE),
        Color(0xFFFFFFFF)
    )
)

val FrostedGlassBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xB3FFFFFF),
        Color(0x66FFFFFF),
        Color(0x80FFFFFF)
    )
)

val GlassBorderBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFFFFF),
        Color(0x66FFFFFF),
        Color(0x22FFFFFF)
    )
)
