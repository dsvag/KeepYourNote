package com.dsvag.keepyournote.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.dsvag.keepyournote.data.database.NoteDatabase
import com.dsvag.keepyournote.data.repository.NoteRepository


class AppComponent(application: Application) {

    private val database by lazy {
        Room.databaseBuilder(application, NoteDatabase::class.java, "note-database").build()
    }
    private val noteDao = database.noteDao()

    private var preferences = application.getSharedPreferences("Settings", Context.MODE_PRIVATE)

    val repository by lazy { NoteRepository(noteDao, preferences) }
}
