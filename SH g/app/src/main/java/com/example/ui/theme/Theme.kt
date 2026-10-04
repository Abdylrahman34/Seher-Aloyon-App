package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LuxuryDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = ObsidianBlack,
    primaryContainer = GoldDark,
    onPrimaryContainer = GoldLight,
    secondary = GoldLight,
    onSecondary = ObsidianBlack,
    secondaryContainer = OnyxCard,
    onSecondaryContainer = GoldLight,
    tertiary = RoseGold,
    onTertiary = ObsidianBlack,
    background = ObsidianBlack,
    onBackground = TextPrimaryDark,
    surface = OnyxSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = OnyxCard,
    onSurfaceVariant = TextSecondaryDark,
    outline = OnyxBorder,
    outlineVariant = OnyxBorder.copy(alpha = 0.5f),
    error = CrimsonVelvet,
    onError = Color.White
)

private val LuxuryLightColorScheme = lightColorScheme(
    primary = GoldDark,
    onPrimary = Color.White,
    primaryContainer = GoldLight,
    onPrimaryContainer = ObsidianBlack,
    secondary = RoseGold,
    onSecondary = Color.White,
    secondaryContainer = CreamSurface,
    onSecondaryContainer = ObsidianBlack,
    tertiary = GoldPrimary,
    onTertiary = ObsidianBlack,
    background = CreamAlabaster,
    onBackground = ObsidianBlack,
    surface = Color.White,
    onSurface = ObsidianBlack,
    surfaceVariant = CreamSurface,
    onSurfaceVariant = TextMuted,
    outline = CreamBorder,
    outlineVariant = CreamBorder.copy(alpha = 0.5f),
    error = CrimsonVelvet,
    onError = Color.White
)

@Composable
fun MaisonAsterTheme(
    darkTheme: Boolean = true, // Default to ultra-luxurious dark haute theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) LuxuryDarkColorScheme else LuxuryLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
