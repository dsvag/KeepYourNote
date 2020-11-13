package com.dsvag.keepyournote.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Note(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("description")
    val description: String,

    @SerializedName("color")
    val color: Int = 0,

    @SerializedName("labels")
    val labels: List<String> = emptyList(),
) : Serializable