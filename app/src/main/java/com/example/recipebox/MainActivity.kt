package com.example.recipebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.recipebox.data.local.RecipeDatabase
import com.example.recipebox.repository.RecipeRepository
import com.example.recipebox.ui.navigation.AppNavigation
import com.example.recipebox.ui.theme.RecipeBoxTheme
import com.example.recipebox.ui.viewmodels.MainViewModel
import com.example.recipebox.ui.viewmodels.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Room Database and Repository
        val database = RecipeDatabase.getDatabase(applicationContext)
        val repository = RecipeRepository(database.recipeDao())
        
        // Initialize ViewModel
        val viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        )[MainViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            RecipeBoxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}