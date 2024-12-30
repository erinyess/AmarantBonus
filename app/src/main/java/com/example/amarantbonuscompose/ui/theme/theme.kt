package com.example.amarantbonuscompose.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFC107),
    onPrimary = Color.White,
    background = Color(0xFF050206),
    onBackground = Color.White,
    surface = Color(0xFF5A5A5A),
    onSurface = Color.White
)

@Composable
fun PromoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}
