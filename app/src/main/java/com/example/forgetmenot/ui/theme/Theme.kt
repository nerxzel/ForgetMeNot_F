package com.example.forgetmenot.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = DarkNavy,
    onPrimary = Color.White,

    tertiary = ForgetMeNotBlue,
    onTertiary = Color.White,

    secondary = CornflowerBlue,
    onSecondary = Color.White,

    background = DarkGreyBackground,
    surface = DarkGreyBackground,
    error = ErrorRed,
    onBackground = LightTextColor,
    onSurface = LightTextColor,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = DarkNavy,
    onPrimary = Color.White,

    tertiary = ForgetMeNotBlue,
    onTertiary = Color.Black,

    secondary = CornflowerBlue,
    onSecondary = Color.White,

    background = Color.White,
    surface = Color.White,
    error = ErrorRed,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

@Composable
fun ForgetMeNot_Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}