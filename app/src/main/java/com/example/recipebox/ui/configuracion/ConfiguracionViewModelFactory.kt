
package com.example.recipebox.ui.configuracion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipebox.data.preferences.UserPreferencesRepository

class ConfiguracionViewModelFactory(
    private val repository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ConfiguracionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfiguracionViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}
