package com.example.ricette.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val DarkColorScheme = darkColorScheme(primary = TerracottaLight, onPrimary = Color(0xFF5F1600), primaryContainer = Color(0xFF882606), onPrimaryContainer = TerracottaContainer, secondary = SageLight, onSecondary = Color(0xFF083820), secondaryContainer = Color(0xFF234E36), onSecondaryContainer = SageContainer, background = WarmDarkBackground, surface = WarmDarkBackground, surfaceContainerLow = WarmDarkSurface, surfaceContainer = WarmDarkSurface)
private val LightColorScheme = lightColorScheme(primary = TerracottaPrimary, onPrimary = OnTerracotta, primaryContainer = TerracottaContainer, onPrimaryContainer = OnTerracottaContainer, secondary = SageSecondary, onSecondary = Color.White, secondaryContainer = SageContainer, onSecondaryContainer = OnSageContainer, background = WarmBackground, surface = WarmBackground, surfaceContainerLow = WarmSurfaceContainer, surfaceContainer = WarmSurfaceContainer)
@Composable
fun RicetteTheme(darkTheme: Boolean = isSystemInDarkTheme(), dynamicColor: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme, typography = Typography, content = content)
}
