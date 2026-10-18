package com.example.recipebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.recipebox.ui.navigation.AppNavigation
import com.example.recipebox.ui.theme.RecipeBoxTheme
import dagger.hilt.android.AndroidEntryPoint

// Hilt requiere @AndroidEntryPoint en todas las Activities que usen inyección de dependencias
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeBoxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // AppNavigation crea sus propios ViewModels con hiltViewModel()
                    AppNavigation()
                }
            }
        }
    }
}