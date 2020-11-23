package com.dsvag.keepyournote.data.database.label

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dsvag.keepyournote.data.models.Label

@Database(entities = [Label::class], version = 1, exportSchema = true)
abstract class LabelDatabase : RoomDatabase() {
    abstract fun labelDao(): LabelDao
}