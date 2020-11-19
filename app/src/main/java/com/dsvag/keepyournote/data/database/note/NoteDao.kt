package com.dsvag.keepyournote.data.database.note

import androidx.room.*
import com.dsvag.keepyournote.data.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>
}