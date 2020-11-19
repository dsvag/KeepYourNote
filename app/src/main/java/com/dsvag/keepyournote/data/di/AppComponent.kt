package com.dsvag.keepyournote.data.di

import android.app.Application
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import com.dsvag.keepyournote.data.database.label.LabelDatabase
import com.dsvag.keepyournote.data.database.note.NoteDatabase
import com.dsvag.keepyournote.data.repository.LabelRepository
import com.dsvag.keepyournote.data.repository.NoteRepository
import com.dsvag.keepyournote.data.repository.ThemeRepository
import com.dsvag.keepyournote.data.utils.KeyBoardUtils

class AppComponent(application: Application) {

    private val noteDatabase by lazy {
        Room.databaseBuilder(application, NoteDatabase::class.java, "note-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    private val noteDao = noteDatabase.noteDao()

    val keyBoardUtils
            by lazy {
                KeyBoardUtils(getSystemService(application, InputMethodManager::class.java)!!)
            }

    val noteRepository by lazy { NoteRepository(noteDao) }

    private val labelDatabase by lazy {
        Room.databaseBuilder(application, LabelDatabase::class.java, "label-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    private val labelDao = labelDatabase.labelDao()

    val labelRepository by lazy { LabelRepository(labelDao) }

    private val dataStore = application.createDataStore(name = "settings")

    val themeRepository by lazy { ThemeRepository(dataStore) }
}
