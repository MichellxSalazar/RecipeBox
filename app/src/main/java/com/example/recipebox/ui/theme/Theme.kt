package com.example.recipebox.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaClaro = lightColorScheme(
    primary = VerdeOliva,
    onPrimary = Blanco,
    primaryContainer = VerdeSuave,
    onPrimaryContainer = VerdeOliva,
    secondary = Terracota,
    onSecondary = Blanco,
    secondaryContainer = CremaOscuro,
    onSecondaryContainer = TextoPrincipal,
    background = Crema,
    onBackground = TextoPrincipal,
    surface = Blanco,
    onSurface = TextoPrincipal,
    surfaceVariant = CremaOscuro,
    onSurfaceVariant = TextoSecundario,
    outline = Contorno,
    outlineVariant = Divisor,
    error = Peligro,
    onError = Blanco,
    surfaceContainerLowest = Blanco,
    surfaceContainerLow = Crema,
    surfaceContainer = Crema,
    surfaceContainerHigh = Blanco,
    surfaceContainerHighest = CremaOscuro
)

@Composable
fun RecipeBoxTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}