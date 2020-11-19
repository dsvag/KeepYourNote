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
    var title: String = "",

    @SerializedName("description")
    var description: String,

    @SerializedName("color")
    var color: Int = 0,

    @SerializedName("labels")
    val labels: MutableList<Label> = mutableListOf(),
) : Serializable {
    fun isNotEmpty() = description.isNotEmpty() || title.isNotEmpty()
}