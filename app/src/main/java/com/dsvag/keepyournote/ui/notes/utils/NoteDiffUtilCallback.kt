package com.dsvag.keepyournote.ui.notes.utils

import androidx.recyclerview.widget.DiffUtil
import com.dsvag.keepyournote.models.Note

class NoteDiffUtilCallback(
    private val newList: List<Note>,
    private val oldList: List<Note>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}