package com.dsvag.keepyournote.data.database.label

import androidx.room.*
import com.dsvag.keepyournote.data.models.Label
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLabel(label: Label)

    @Delete
    suspend fun deleteLabel(label: Label)

    @Query("SELECT * FROM label")
    fun getLabels(): Flow<List<Label>>
}