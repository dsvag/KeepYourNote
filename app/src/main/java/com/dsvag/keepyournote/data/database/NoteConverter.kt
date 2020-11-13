package com.dsvag.keepyournote.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NoteConverter {
    @TypeConverter
    fun toLabels(labels: String): List<String> {
        val labelsType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(labels, labelsType)
    }

    @TypeConverter
    fun fromLabels(labels: List<String>): String = Gson().toJson(labels)

}