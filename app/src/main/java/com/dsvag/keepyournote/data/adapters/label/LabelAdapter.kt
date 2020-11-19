package com.dsvag.keepyournote.data.adapters.label

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.keepyournote.data.models.Label
import com.dsvag.keepyournote.databinding.RowLabelBinding
import kotlin.math.min

class LabelAdapter : RecyclerView.Adapter<LabelAdapter.LabelViewHolder>() {

    private val labels: MutableList<Label> = mutableListOf()

    private var isInNote = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LabelViewHolder(RowLabelBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        if (isInNote && position == 2 && labels.size > 3) {
            holder.bind(Label(title = "+${labels.size - 2}"))
        } else {
            holder.bind(labels[position])
        }
    }

    override fun getItemCount(): Int {
        return if (isInNote) {
            min(labels.size, 3)
        } else {
            labels.size
        }
    }

    fun setData(list: List<Label>) {
        labels.clear()
        labels.addAll(list)
        notifyDataSetChanged()
    }

    fun setIsInNote(boolean: Boolean) {
        isInNote = boolean
    }

    class LabelViewHolder(private val itemBinding: RowLabelBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(label: Label) {
            itemBinding.label.text = label.title
        }
    }
}