package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ElectricLime,
    onPrimary = MidnightBackground,
    primaryContainer = ElectricLime,
    onPrimaryContainer = MidnightBackground,
    secondary = BrightCyan,
    onSecondary = MidnightBackground,
    secondaryContainer = BrightCyan,
    onSecondaryContainer = MidnightBackground,
    error = PulseRed,
    onError = MidnightBackground,
    background = MidnightBackground,
    onBackground = OnSurface,
    surface = MidnightSurface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = OnSurfaceVariant,
    outline = OutlineVariant,
    outlineVariant = OutlineColor
)

@Composable
fun MyApplicationTheme(
    // PulseFit is exclusively dark theme as per the high-end technical athletic HUD specs
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // We enforce the Vibrant Performance theme colors
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
