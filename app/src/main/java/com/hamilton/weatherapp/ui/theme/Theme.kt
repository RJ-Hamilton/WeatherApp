package com.hamilton.weatherapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = OceanTeal,
    secondary = OceanTeal,
    tertiary = DarkOceanTeal,
    background = OffWhite,
    surface = OffWhite,
    onPrimary = Black,
    onSecondary = Black,
    onSurface = Black
)

private val LightColorScheme = lightColorScheme(
    primary = OceanTeal,
    secondary = OffWhite,
    tertiary = DarkOceanTeal,
    background = OffWhite,
    surface = OffWhite,
    onPrimary = Black,
    onSecondary = Black,
    onSurface = Black
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (!darkTheme) {
        LightColorScheme
    } else {
        DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}