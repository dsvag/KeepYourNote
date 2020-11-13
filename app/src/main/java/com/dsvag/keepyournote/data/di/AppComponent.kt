package com.dsvag.keepyournote.data.di

import android.app.Application
import androidx.room.Room
import com.dsvag.keepyournote.data.database.NoteDatabase
import com.dsvag.keepyournote.data.repository.NoteRepository

class AppComponent(application: Application) {

    private val database by lazy {
        Room.databaseBuilder(application, NoteDatabase::class.java, "note-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    private val noteDao = database.noteDao()

    val repository by lazy { NoteRepository(noteDao) }
}
