package com.dsvag.keepyournote.data.di

import android.app.Application
import androidx.room.Room
import com.dsvag.keepyournote.data.database.label.LabelDatabase
import com.dsvag.keepyournote.data.database.note.NoteDatabase
import com.dsvag.keepyournote.data.repository.LabelRepository
import com.dsvag.keepyournote.data.repository.NoteRepository

class AppComponent(application: Application) {

    private val noteDatabase by lazy {
        Room.databaseBuilder(application, NoteDatabase::class.java, "note-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    private val noteDao = noteDatabase.noteDao()

    val noteRepository by lazy { NoteRepository(noteDao) }

    private val labelDatabase by lazy {
        Room.databaseBuilder(application, LabelDatabase::class.java, "label-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    private val labelDao = labelDatabase.labelDao()

    val labelRepository by lazy { LabelRepository(labelDao) }
}
