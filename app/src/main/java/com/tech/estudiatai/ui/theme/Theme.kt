package com.tech.estudiatai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Immutable

/*
@Composable
fun EstudiaTAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
       // colorScheme = colorScheme,
        LightColorScheme,
        typography = Typography,
        content = content
    )
}*/

/* Con el modo toggle
fun EstudiaTAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}*/


/* Cambios de Adrian

@Composable
fun EstudiaTAITheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) {
        PaletaDeColores.darkColors
    } else {
        PaletaDeColores.lightColors
    }

    MaterialTheme(
        // No usamos el colorScheme aquí
        content = content
    )
}
*/

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = primaryTextLight,
    secondary = secondaryLight,
    onSecondary = secondaryTextLight,
    tertiary = tertiaryLight,
    onTertiary = tertiaryTextLight,
    error = errorLight,
    background = backgroundLight,
    onBackground = onbackgroundLight,
    surface = surfaceLight
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = primaryTextDark,
    secondary = secondaryDark,
    onSecondary = secondaryTextDark,
    tertiary = tertiaryDark,
    onTertiary = tertiaryTextDark,
    error = errorDark,
    background = backgroundDark,
    onBackground = onbackgroundDark,
    surface = surfaceDark
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

