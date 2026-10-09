package com.ankangcare.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Teal600,
    onPrimary = Color.White,
    primaryContainer = Teal50,
    onPrimaryContainer = Teal700,
    secondary = Amber500,
    error = Rose500,
    background = Slate50,
    surface = Color.White,
    onBackground = Slate900,
    onSurface = Slate900
)

private val DarkColorScheme = darkColorScheme(
    primary = Teal600,
    onPrimary = Color.White,
    primaryContainer = Slate800,
    onPrimaryContainer = Teal100,
    secondary = Amber500,
    error = Rose500,
    background = Slate900,
    surface = Slate800,
    onBackground = Slate50,
    onSurface = Slate50
)

@Composable
fun AnkangCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
