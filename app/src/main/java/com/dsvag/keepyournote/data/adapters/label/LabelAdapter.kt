package com.dsvag.keepyournote.data.adapters.label

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.keepyournote.data.models.Label
import com.dsvag.keepyournote.databinding.RowLabelPickerBinding

class LabelAdapter(
    private val onClickCallback: (label: Label) -> Unit,
) : RecyclerView.Adapter<LabelAdapter.LabelViewHolder>() {

    private val labels: MutableList<Label> = mutableListOf()
    private val checkedLabels: MutableList<Label> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LabelViewHolder(
            RowLabelPickerBinding.inflate(inflater, parent, false),
            ::addToChecked,
            ::removeFromChecked,
            ::onClick,
        )
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        holder.bind(labels[position])
    }

    override fun getItemCount(): Int = labels.size

    fun setData(newLabels: List<Label>, diffResult: DiffUtil.DiffResult) {
        labels.clear()
        labels.addAll(newLabels)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getData() = labels

    private fun onClick(label: Label) {
        onClickCallback(label)
    }

    private fun addToChecked(position: Int) {
        checkedLabels.add(labels[position])
    }

    private fun removeFromChecked(label: Label) {
        checkedLabels.remove(label)
    }

    class LabelViewHolder(
        private val itemBinding: RowLabelPickerBinding,
        private val add: (position: Int) -> Unit,
        private val remove: (label: Label) -> Unit,
        private val onClick: (label: Label) -> Unit,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(label: Label) {
            itemBinding.text.text = label.title
            itemBinding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    add(adapterPosition)
                } else {
                    remove(label)
                }
            }

            itemBinding.root.setOnClickListener {
                onClick(label)
            }
        }
    }
}