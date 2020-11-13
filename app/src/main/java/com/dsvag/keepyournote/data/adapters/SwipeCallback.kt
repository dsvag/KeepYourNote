package com.dsvag.keepyournote.data.adapters

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeCallback(
    private val leftSwipeAction: (Int) -> Unit,
    private val leftSwipeIcon: Drawable,
    private val leftSwipeIconColor: Int,
    private val rightSwipeAction: (Int) -> Unit = leftSwipeAction,
    private val rightSwipeIcon: Drawable = leftSwipeIcon,
    private val rightSwipeIconColor: Int = leftSwipeIconColor,
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.LEFT -> leftSwipeAction(viewHolder.adapterPosition)
            ItemTouchHelper.RIGHT -> rightSwipeAction(viewHolder.adapterPosition)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val leftIconMargin = leftSwipeIcon.intrinsicWidth
        val rightIconMargin = rightSwipeIcon.intrinsicWidth

        when {
            dX > 0 -> { // to right
                val iconTop = itemView.top + (itemView.height - rightSwipeIcon.intrinsicHeight) / 2
                val iconBottom = iconTop + rightSwipeIcon.intrinsicHeight
                val iconLeft = itemView.left + rightIconMargin
                val iconRight = itemView.left + rightIconMargin + rightSwipeIcon.intrinsicWidth

                rightSwipeIcon.setTint(rightSwipeIconColor)
                rightSwipeIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            }

            dX < 0 -> { // to left
                val iconTop = itemView.top + (itemView.height - leftSwipeIcon.intrinsicHeight) / 2
                val iconBottom = iconTop + leftSwipeIcon.intrinsicHeight
                val iconLeft = itemView.right - leftIconMargin - leftSwipeIcon.intrinsicWidth
                val iconRight = itemView.right - leftIconMargin

                leftSwipeIcon.setTint(leftSwipeIconColor)
                leftSwipeIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            }
        }

        leftSwipeIcon.draw(c)
        rightSwipeIcon.draw(c)
    }
}