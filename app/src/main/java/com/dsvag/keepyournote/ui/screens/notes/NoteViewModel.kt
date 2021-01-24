package com.dsvag.keepyournote.ui.screens.notes

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dsvag.keepyournote.data.repository.NoteRepository
import com.dsvag.keepyournote.models.Note
import com.dsvag.keepyournote.utils.KeyBoardUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel @ViewModelInject constructor(
    private val noteRepository: NoteRepository,
    private val keyBoardUtils: KeyBoardUtils,
) : ViewModel() {

    val notes = noteRepository.getNotes().asLiveData()

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insertNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(note)
        }
    }

    fun showKeyBoard(view: View) {
        keyBoardUtils.showKeyBoard(view)
    }

    fun hideKeyBoard(view: View) {
        keyBoardUtils.hideKeyBoard(view)
    }
}