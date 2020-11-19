package com.dsvag.keepyournote.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepository(private val dataStore: DataStore<Preferences>) {

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

    private val themeKey = preferencesKey<Int>("theme")
}