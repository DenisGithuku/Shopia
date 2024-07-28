package com.githukudenis.core_design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Green200,
    primaryVariant = Cyan200,
    secondary = Pink700,
    background = PureDark,
    onBackground = NeutralWhite,
    surface = NeutralWhite,
)

private val LightColorPalette = lightColors(
    primary = Green500,
    primaryVariant = Green200,
    secondary = Cyan200,
    background = NeutralWhite,
    surface = DefaultWhite,
    onPrimary = DefaultWhite,
    onSecondary = NeutralDark,
    onBackground = NeutralDark,
    onSurface = NeutralDark,
)

@Composable
fun CoroutinesIndustrialBuildTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}