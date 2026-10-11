package com.example.recipebox.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val KEEP_SCREEN_ON = booleanPreferencesKey("keep_screen_on")
        val USE_METRIC_SYSTEM = booleanPreferencesKey("use_metric_system")
    }

    val keepScreenOnFlow: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEEP_SCREEN_ON] ?: false
        }

    val useMetricSystemFlow: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USE_METRIC_SYSTEM] ?: true
        }

    suspend fun updateKeepScreenOn(keepOn: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEEP_SCREEN_ON] = keepOn
        }
    }

    suspend fun updateUseMetricSystem(useMetric: Boolean) {
        dataStore.edit { preferences ->
            preferences[USE_METRIC_SYSTEM] = useMetric
        }
    }
}
