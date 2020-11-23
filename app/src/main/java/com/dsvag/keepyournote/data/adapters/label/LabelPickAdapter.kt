package com.dsvag.keepyournote.data.adapters.label

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.keepyournote.data.models.Label
import com.dsvag.keepyournote.databinding.RowLabelPickerBinding

class LabelPickAdapter(
    private val onLabelClick: (label: Label) -> Unit,
) : RecyclerView.Adapter<LabelPickAdapter.LabelPickViewHolder>() {

    var labels: MutableList<Label> = mutableListOf()
        private set

    var checkedLabels: MutableList<Label> = mutableListOf()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelPickViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LabelPickViewHolder(
            RowLabelPickerBinding.inflate(inflater, parent, false),
            onLabelClick,
            ::onStateChanged,
        )
    }

    override fun onBindViewHolder(holder: LabelPickViewHolder, position: Int) {
        holder.bind(labels[position])
    }

    override fun getItemCount(): Int = labels.size

    fun setData(newLabels: List<Label>, diffResult: DiffUtil.DiffResult) {
        labels.clear()
        labels.addAll(newLabels)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun onStateChanged(state: Boolean, label: Label) {
        if (state) {
            checkedLabels.add(label)
        } else {
            checkedLabels.remove(label)
        }
    }

    class LabelPickViewHolder(
        private val itemBinding: RowLabelPickerBinding,
        private val onClick: (label: Label) -> Unit,
        private val onStateChanged: (Boolean, Label) -> Unit,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(label: Label) {
            itemBinding.text.text = label.title
            itemBinding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                onStateChanged(isChecked, label)
            }

            itemBinding.root.setOnClickListener {
                onClick(label)
            }
        }
    }
}