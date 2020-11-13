package com.dsvag.keepyournote.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dsvag.keepyournote.data.models.Note

@TypeConverters(NoteConverter::class)
@Database(entities = [Note::class], version = 3, exportSchema = true)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}