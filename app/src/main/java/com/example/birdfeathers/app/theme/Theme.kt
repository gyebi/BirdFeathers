package com.example.birdfeathers.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.birdfeathers.app.theme.md_theme_dark_background
import com.example.birdfeathers.app.theme.md_theme_dark_onBackground
import com.example.birdfeathers.app.theme.md_theme_dark_onPrimary
import com.example.birdfeathers.app.theme.md_theme_dark_onSecondary
import com.example.birdfeathers.app.theme.md_theme_dark_onSurface
import com.example.birdfeathers.app.theme.md_theme_dark_primary
import com.example.birdfeathers.app.theme.md_theme_dark_secondary
import com.example.birdfeathers.app.theme.md_theme_dark_surface
import com.example.birdfeathers.app.theme.md_theme_dark_surfaceVariant
import com.example.birdfeathers.app.theme.md_theme_light_background
import com.example.birdfeathers.app.theme.md_theme_light_onBackground
import com.example.birdfeathers.app.theme.md_theme_light_onPrimary
import com.example.birdfeathers.app.theme.md_theme_light_onSecondary
import com.example.birdfeathers.app.theme.md_theme_light_onSurface
import com.example.birdfeathers.app.theme.md_theme_light_primary
import com.example.birdfeathers.app.theme.md_theme_light_secondary
import com.example.birdfeathers.app.theme.md_theme_light_surface
import com.example.birdfeathers.app.theme.md_theme_light_surfaceVariant

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant
)

private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant
)

@Composable
fun BirdFeathersTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
