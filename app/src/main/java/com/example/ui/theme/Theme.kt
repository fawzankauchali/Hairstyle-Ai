package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = DarkObsidian,
    primaryContainer = GoldDark,
    onPrimaryContainer = GoldLight,
    secondary = CyanAccent,
    onSecondary = DarkObsidian,
    secondaryContainer = Color(0xFF1E3A3A),
    onSecondaryContainer = CyanBright,
    tertiary = GoldLight,
    onTertiary = DarkObsidian,
    background = DarkObsidian,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder,
    error = ErrorRed
)

private val LightColorScheme = darkColorScheme(
    // Keep high-contrast modern deep theme for visual luxury barbershop experience
    primary = GoldPrimary,
    onPrimary = DarkObsidian,
    primaryContainer = GoldDark,
    onPrimaryContainer = GoldLight,
    secondary = CyanAccent,
    onSecondary = DarkObsidian,
    background = DarkObsidian,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
