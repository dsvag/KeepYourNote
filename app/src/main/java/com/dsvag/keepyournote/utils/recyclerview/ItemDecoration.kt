package com.dsvag.keepyournote.utils.recyclerview

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(private val pxMargin: Float) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val margin = convertPxToDp(parent.context, pxMargin)

        outRect.right = margin
        outRect.bottom = margin
        outRect.top = margin
        outRect.left = margin
    }

    private fun convertPxToDp(context: Context, px: Float): Int {
        return (px / context.resources.displayMetrics.density).toInt()
    }
}