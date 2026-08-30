package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AlfaDarkColorScheme = darkColorScheme(
    primary = AlfaNeonLime,
    onPrimary = AlfaBlack,
    primaryContainer = AlfaSurfaceElevated,
    onPrimaryContainer = AlfaNeonLime,
    secondary = AlfaNeonLimeLight,
    onSecondary = AlfaBlack,
    secondaryContainer = AlfaSurfaceCard,
    onSecondaryContainer = AlfaTextWhite,
    tertiary = AlfaNeonGreen,
    onTertiary = AlfaBlack,
    background = AlfaBlack,
    onBackground = AlfaTextWhite,
    surface = AlfaSurfaceDark,
    onSurface = AlfaTextWhite,
    surfaceVariant = AlfaSurfaceCard,
    onSurfaceVariant = AlfaTextGray,
    outline = AlfaBorder,
    outlineVariant = AlfaBorderSubtle,
    error = AlfaError,
    onError = Color.White
)

@Composable
fun AlfaFitTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AlfaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
