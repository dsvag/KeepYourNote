package com.dsvag.keepyournote.data.repository

import com.dsvag.keepyournote.data.database.note.NoteDao
import com.dsvag.keepyournote.models.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {
    fun getNotes() = noteDao.getNotes()

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun clear() {
        noteDao.clear()
    }
}