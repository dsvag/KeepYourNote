package com.dsvag.keepyournote.data.repository

import com.dsvag.keepyournote.data.database.label.LabelDao
import com.dsvag.keepyournote.data.models.Label

class LabelRepository(
    private val labelDao: LabelDao,
) {
    fun getLabels() = labelDao.getLabels()

    suspend fun insertLabel(label: Label) {
        labelDao.insertLabel(label)
    }

    suspend fun deleteLabel(label: Label) {
        labelDao.deleteLabel(label)
    }

}