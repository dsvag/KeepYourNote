package com.dsvag.keepyournote.data.database.note

import androidx.room.TypeConverter
import com.dsvag.keepyournote.data.models.Label
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NoteConverter {
    @TypeConverter
    fun toLabels(labels: String): List<Label> {
        val labelsType = object : TypeToken<List<Label>>() {}.type
        return Gson().fromJson(labels, labelsType)
    }

    @TypeConverter
    fun fromLabels(labels: List<Label>): String = Gson().toJson(labels)

}