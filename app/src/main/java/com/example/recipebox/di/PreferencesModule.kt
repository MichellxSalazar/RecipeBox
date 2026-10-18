
package com.example.recipebox.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.recipebox.data.preferences.UserPreferencesRepository
import com.example.recipebox.data.preferences.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        dataStore: DataStore<Preferences>
    ): UserPreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }
}
