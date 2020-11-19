package com.dsvag.keepyournote.data.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.dsvag.keepyournote.data.di.getAppComponent
import com.dsvag.keepyournote.data.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = application.getAppComponent().noteRepository

    val keyBoardUtils = application.getAppComponent().keyBoardUtils

    val getNotes = repository.getNotes().asLiveData()

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NoteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}