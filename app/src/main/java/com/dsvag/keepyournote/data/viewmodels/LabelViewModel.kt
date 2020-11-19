package com.dsvag.keepyournote.data.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.dsvag.keepyournote.data.di.getAppComponent
import com.dsvag.keepyournote.data.models.Label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LabelViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = application.getAppComponent().labelRepository

    val getLabels = repository.getLabels().asLiveData()

    fun insertLabel(label: Label) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLabel(label)
        }
    }

    fun deleteLabel(label: Label) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLabel(label)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LabelViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LabelViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}