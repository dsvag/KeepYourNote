package com.dsvag.keepyournote.data.adapters.color

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.keepyournote.R
import com.dsvag.keepyournote.databinding.RowColorBinding
import com.dsvag.keepyournote.ui.sheets.ColorSheet

class ColorAdapter : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var colors = intArrayOf()

    private var onClickListener: ((Int) -> Unit) = {}
    private var dialog: ColorSheet? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ColorViewHolder(
            RowColorBinding.inflate(inflater, parent, false),
            onClickListener,
            dialog,
        )
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position])
    }

    override fun getItemCount(): Int = colors.size

    fun setData(dialog: ColorSheet, newColors: IntArray) {
        colors = newColors
        this.dialog = dialog
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClick: (Int) -> Unit) {
        onClickListener = onClick
    }

    class ColorViewHolder(
        private val itemBinding: RowColorBinding,
        private val onClickListener: (Int) -> Unit,
        private val dialog: ColorSheet?,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(color: Int) {
            if (adapterPosition == 0) {
                itemBinding.color.backgroundTintList =
                    ContextCompat.getColorStateList(itemBinding.root.context, R.color.not_black)

                itemBinding.color.setImageDrawable(
                    ContextCompat.getDrawable(itemBinding.root.context, R.drawable.ic_invisible)
                )
            } else {
                itemBinding.color.backgroundTintList = ColorStateList.valueOf(color)
            }

            itemBinding.color.setOnClickListener {
                onClickListener(color)
                dialog?.dismiss()
            }
        }
    }
}