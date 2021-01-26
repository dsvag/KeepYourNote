package com.dsvag.keepyournote.ui.screens.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dsvag.keepyournote.data.repository.SettingsRepository
import com.dsvag.keepyournote.utils.ThemeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val theme = settingsRepository.getTheme().asLiveData()

    fun setTheme(themeType: ThemeType) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.setTheme(themeType.type)
        }
    }
}