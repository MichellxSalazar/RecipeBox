
package com.example.recipebox.ui.configuracion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipebox.data.preferences.UserPreferencesRepository
import com.example.recipebox.domain.model.PreferenciasUsuario
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ConfiguracionViewModel @Inject constructor(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    val uiState = combine(
        repository.keepScreenOnFlow,
        repository.useMetricSystemFlow
    ) { mantenerPantalla, sistemaMetrico ->
        PreferenciasUsuario(
            mantenerPantallaEncendida = mantenerPantalla,
            usarSistemaMetrico = sistemaMetrico
        )
    }
        .map<PreferenciasUsuario, ConfiguracionUiState> { preferencias ->
            ConfiguracionUiState.Exito(preferencias)
        }
        .catch { error ->
            emit(
                ConfiguracionUiState.Error(
                    mensaje = error.message
                        ?: "No se pudieron cargar las preferencias"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ConfiguracionUiState.Cargando
        )

    fun cambiarPantallaEncendida(activar: Boolean) {
        viewModelScope.launch {
            repository.updateKeepScreenOn(activar)
        }
    }

    fun cambiarSistemaMetrico(activar: Boolean) {
        viewModelScope.launch {
            repository.updateUseMetricSystem(activar)
        }
    }
}
