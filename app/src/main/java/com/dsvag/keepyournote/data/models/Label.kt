package com.dsvag.keepyournote.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Label(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @SerializedName("title")
    val title: String,
) : Serializable