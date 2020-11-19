package com.dsvag.keepyournote.data.adapters.note

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.data.adapters.label.LabelAdapter
import com.dsvag.keepyournote.data.models.Note
import com.dsvag.keepyournote.databinding.RowNoteBinding

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
                putSerializable("note", noteList[position])
            }
            holder.itemView.findNavController().navigate(R.id.noteFragment, bundle)
        }
    }

    override fun getItemCount() = noteList.size

    override fun onViewDetachedFromWindow(holder: NoteViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unBind()
    }

    fun setData(noteList: List<Note>, diffResult: DiffUtil.DiffResult) {
        this.noteList.clear()
        this.noteList.addAll(noteList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int) = noteList[position]

    fun getData(): List<Note> = noteList

    class NoteViewHolder(private val itemBinding: RowNoteBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val adapter = LabelAdapter()

        fun bind(note: Note) {
            if (note.title.isEmpty()) itemBinding.title.visibility = View.GONE
            if (note.description.isEmpty()) itemBinding.description.visibility = View.GONE

            itemBinding.title.text = note.title
            itemBinding.description.text = note.description

            val background = itemBinding.root.background!! as GradientDrawable

            if (note.color != -1) {
                background.setStroke(4, note.color)
            } else {
                background.setStroke(0, note.color)
            }

            itemBinding.labels.setHasFixedSize(true)
            itemBinding.labels.layoutManager =
                LinearLayoutManager(itemBinding.root.context, LinearLayoutManager.HORIZONTAL, false)
            itemBinding.labels.adapter = adapter
            adapter.setData(note.labels)
            adapter.setIsInNote(true)
        }

        fun unBind() {
            adapter.setData(emptyList())
        }
    }
}
