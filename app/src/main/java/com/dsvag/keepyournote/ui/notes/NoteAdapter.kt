package com.dsvag.keepyournote.ui.notes

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.databinding.RowNoteBinding
import com.dsvag.keepyournote.models.Note
import com.dsvag.keepyournote.ui.notes.utils.NoteDiffUtilCallback

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val noteList: MutableList<Note> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(RowNoteBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(noteList[position])
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
            itemBinding.title.isVisible = note.title.isNotEmpty()
            itemBinding.description.isVisible = note.description.isNotEmpty()

            itemBinding.title.text = note.title
            itemBinding.description.text = note.description

            val background = itemBinding.root.background!! as GradientDrawable
            val noteStrokeWidth =
                itemBinding.root.resources.getDimension(R.dimen.noteStrokeWidth).toInt()

            background.setStroke(noteStrokeWidth, note.color)

            itemBinding.root.setOnClickListener {
                val bundle = Bundle().apply { putParcelable("note", note) }

                itemBinding.root.findNavController()
                    .navigate(R.id.action_noteListFragment_to_noteFragment, bundle)
            }
        }
    }
}
