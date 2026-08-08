package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = TrustLensPrimaryFixedDim,
    onPrimary = TrustLensPrimary,
    primaryContainer = TrustLensPrimaryContainer,
    onPrimaryContainer = TrustLensOnPrimaryContainer,
    secondary = TrustLensSecondaryFixedDim,
    onSecondary = TrustLensPrimary,
    secondaryContainer = TrustLensSecondary,
    onSecondaryContainer = TrustLensOnSecondary,
    background = TrustLensPrimary,
    onBackground = TrustLensBackground,
    surface = TrustLensPrimaryContainer,
    onSurface = TrustLensBackground,
    surfaceVariant = TrustLensSurfaceTint,
    onSurfaceVariant = TrustLensOutlineVariant,
    outline = TrustLensOutline,
    outlineVariant = TrustLensSurfaceTint,
    error = TrustLensError,
    errorContainer = TrustLensErrorContainer,
    onError = TrustLensOnError,
    onErrorContainer = TrustLensOnErrorContainer
)

private val LightColorScheme = lightColorScheme(
    primary = TrustLensPrimary,
    onPrimary = TrustLensOnPrimary,
    primaryContainer = TrustLensPrimaryContainer,
    onPrimaryContainer = TrustLensOnPrimaryContainer,
    secondary = TrustLensSecondary,
    onSecondary = TrustLensOnSecondary,
    secondaryContainer = TrustLensSecondaryContainer,
    onSecondaryContainer = TrustLensOnSecondaryContainer,
    background = TrustLensBackground,
    onBackground = TrustLensOnBackground,
    surface = TrustLensSurface,
    onSurface = TrustLensOnSurface,
    surfaceVariant = TrustLensSurfaceVariant,
    onSurfaceVariant = TrustLensOnSurfaceVariant,
    outline = TrustLensOutline,
    outlineVariant = TrustLensOutlineVariant,
    error = TrustLensError,
    errorContainer = TrustLensErrorContainer,
    onError = TrustLensOnError,
    onErrorContainer = TrustLensOnErrorContainer
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Standard light compliance theme by default for precision & high contrast
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TrustLensTypography,
        content = content
    )
}
