package com.dsvag.keepyournote.ui.screens.notes

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.databinding.RowNoteBinding
import com.dsvag.keepyournote.models.Note
import com.dsvag.keepyournote.ui.screens.notes.utils.NoteDiffUtilCallback

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val noteList: MutableList<Note> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(RowNoteBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(noteList[position])

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("note", noteList[position])
            }
            holder.itemView.findNavController().navigate(R.id.noteFragment, bundle)
        }
    }

    override fun getItemCount() = noteList.size

    fun setData(newNoteList: List<Note>) {
        val diffResult = DiffUtil.calculateDiff(NoteDiffUtilCallback(newNoteList, noteList))

        noteList.clear()
        noteList.addAll(newNoteList)

        diffResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int) = noteList[position]

    class NoteViewHolder(private val itemBinding: RowNoteBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(note: Note) {
            if (note.title.isEmpty()) itemBinding.title.visibility = View.GONE
            if (note.description.isEmpty()) itemBinding.description.visibility = View.GONE

            itemBinding.title.text = note.title
            itemBinding.description.text = note.description

            val background = itemBinding.root.background!! as GradientDrawable

            background.setStroke(4, note.color)
        }
    }
}
