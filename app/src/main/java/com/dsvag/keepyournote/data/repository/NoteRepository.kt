package com.dsvag.keepyournote.data.repository

import androidx.lifecycle.asLiveData
import com.dsvag.keepyournote.data.database.note.NoteDao
import com.dsvag.keepyournote.data.models.Note

class NoteRepository(
    private val noteDao: NoteDao,
) {
    fun getNoteFromDb() = noteDao.getNotes().asLiveData()

    suspend fun insertNote(note: Note) {
        noteDao.addNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}