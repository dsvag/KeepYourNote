package com.dsvag.keepyournote.data.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.asLiveData
import com.dsvag.keepyournote.data.database.NoteDao
import com.dsvag.keepyournote.data.models.Note
import com.dsvag.keepyournote.data.utils.Theme

class NoteRepository(
    private val noteDao: NoteDao,
    private val preferences: SharedPreferences,
) {
    private val editor = preferences.edit()
    fun getNoteFromDb() = noteDao.getNotes().asLiveData()

    suspend fun insertNote(note: Note) {
        noteDao.addNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    fun getTheme(): Theme {
        val theme =
            preferences.getString("theme", Theme.SYSTEM.toString()) ?: Theme.SYSTEM.toString()

        return when (theme) {
            "LIGHT" -> Theme.LIGHT
            "DARK" -> Theme.DARK
            "SYSTEM" -> Theme.SYSTEM
            else -> error("unknown theme type")
        }
    }

    fun setTheme(theme: Theme) {
        editor.putString("theme", theme.toString())
        editor.commit()
    }
}