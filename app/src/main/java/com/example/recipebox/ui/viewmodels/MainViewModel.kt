
package com.example.recipebox.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebox.data.local.entity.RecipeEntity
import com.example.recipebox.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    val allRecipes: StateFlow<List<RecipeEntity>> =
        repository.getAllRecipes().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            repository.insertRecipe(recipe)
        }
    }
}
