package com.dsvag.keepyournote.data.adapters.label

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.keepyournote.databinding.RowLabelFullBinding

class LabelAdapter : RecyclerView.Adapter<LabelAdapter.LabelViewHolder>() {

    private val labels: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LabelViewHolder(RowLabelFullBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        holder.bind(labels[position])
    }

    override fun getItemCount(): Int = labels.size

    fun setData(newLabels: List<String>) {
        labels.clear()
        labels.addAll(newLabels)
        notifyDataSetChanged()
    }

    class LabelViewHolder(
        private val itemBinding: RowLabelFullBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(label: String) {
            itemBinding.textInputEditText.setText(label)
        }
    }
}