package com.dsvag.keepyournote.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun getTheme(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: 2
        }
    }

    suspend fun setTheme(themeType: Int) {
        dataStore.edit { preferences ->
            preferences[themeKey] = themeType
        }
    }

    private val themeKey = intPreferencesKey("theme")
}