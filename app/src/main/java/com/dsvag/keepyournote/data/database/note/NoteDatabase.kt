package com.dsvag.keepyournote.data.database.note

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dsvag.keepyournote.models.Note

@Database(entities = [Note::class], version = 1, exportSchema = true)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}