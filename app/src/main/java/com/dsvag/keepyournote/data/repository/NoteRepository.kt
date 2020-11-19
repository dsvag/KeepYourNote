package com.dsvag.keepyournote.data.repository

import com.dsvag.keepyournote.data.database.note.NoteDao
import com.dsvag.keepyournote.data.models.Note

class NoteRepository(
    private val noteDao: NoteDao,
) {
    fun getNotes() = noteDao.getNotes()

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}