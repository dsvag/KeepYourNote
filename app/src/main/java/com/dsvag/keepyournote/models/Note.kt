package com.dsvag.keepyournote.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable