package com.example.businessmanager.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Color(0xFF2F5BEA),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDE3FF),
    onPrimaryContainer = Color(0xFF00164F),
    secondary = Color(0xFF00897B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB9F2E8),
    onSecondaryContainer = Color(0xFF00201C),
    tertiary = Color(0xFFEF6C00),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDCC2),
    onTertiaryContainer = Color(0xFF301400),
    background = Color(0xFFF6F7FB),
    onBackground = Color(0xFF1B1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1B1B1F),
    surfaceVariant = Color(0xFFE8EAF3),
    onSurfaceVariant = Color(0xFF454A5C),
    outlineVariant = Color(0xFFD5D8E4)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB7C4FF),
    onPrimary = Color(0xFF00267E),
    primaryContainer = Color(0xFF1F3FB8),
    onPrimaryContainer = Color(0xFFDDE3FF),
    secondary = Color(0xFF7FD9CB),
    onSecondary = Color(0xFF003731),
    secondaryContainer = Color(0xFF005048),
    onSecondaryContainer = Color(0xFFB9F2E8),
    tertiary = Color(0xFFFFB782),
    onTertiary = Color(0xFF4F2500),
    tertiaryContainer = Color(0xFF703700),
    onTertiaryContainer = Color(0xFFFFDCC2),
    background = Color(0xFF111318),
    onBackground = Color(0xFFE3E2E9),
    surface = Color(0xFF1A1C22),
    onSurface = Color(0xFFE3E2E9),
    surfaceVariant = Color(0xFF2B2E3A),
    onSurfaceVariant = Color(0xFFC5C8D6),
    outlineVariant = Color(0xFF3A3D4A)
)

private val BizShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun BizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // true = Material You colors on Android 12+
    content: @Composable () -> Unit
) {
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(colorScheme = colors, shapes = BizShapes, content = content)
}
