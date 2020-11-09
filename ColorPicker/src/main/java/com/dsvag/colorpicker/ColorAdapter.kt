package com.dsvag.colorpicker

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.colorpicker.databinding.RowColorBinding

class ColorAdapter(
    private val colorList: Array<Int>,
    private val listener: ColorPickerListener
) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ColorViewHolder(RowColorBinding.inflate(inflater, parent, false), listener)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colorList[position])
    }

    override fun getItemCount() = colorList.size

    class ColorViewHolder(
        private val itemBinding: RowColorBinding,
        private val listener: ColorPickerListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(color: Int) {
            val background = itemBinding.color.background as GradientDrawable
            background.setColor(color)

            itemBinding.color.background = background

            itemBinding.color.setOnClickListener { listener }
        }
    }
}