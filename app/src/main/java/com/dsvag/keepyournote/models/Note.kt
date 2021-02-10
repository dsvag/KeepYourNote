package com.dsvag.keepyournote.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String = "",

    val description: String = "",

    val color: Int = 0,
) : Parcelable