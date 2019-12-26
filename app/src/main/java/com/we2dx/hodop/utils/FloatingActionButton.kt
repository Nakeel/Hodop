package com.we2dx.drCloud.utils


import android.content.ClipData
import android.view.DragEvent
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Extension Class for {@link FloatingActionButton}
 *
 *  created on 08/11/2017
 *
 *  @version 1.0
 *  @author tfi
 */

fun FloatingActionButton.setDragDrop(target: View) {
    setOnLongClickListener { view ->
        ViewCompat.startDragAndDrop(view, ClipData.newPlainText("", ""), View.DragShadowBuilder(view), null, 0)
    }

    setOnDragListener { view, dragEvent ->
        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_ENTERED, DragEvent.ACTION_DRAG_STARTED -> view.visibility = View.INVISIBLE
            DragEvent.ACTION_DRAG_ENDED -> view.visibility = View.VISIBLE
        }
        true
    }
    target.setOnDragListener { _, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            setPositionNoGravity(event.x.toInt(), event.y.toInt())
        }
        true
    }
}

private fun FloatingActionButton.setPositionNoGravity(x: Int, y: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    layoutParams = params.apply {
        when (this) {
            is CoordinatorLayout.LayoutParams -> gravity = Gravity.NO_GRAVITY
            is FrameLayout.LayoutParams -> gravity = Gravity.NO_GRAVITY
            is LinearLayout.LayoutParams -> gravity = Gravity.NO_GRAVITY
        }
        leftMargin = x - this@setPositionNoGravity.width / 2
        topMargin = y - this@setPositionNoGravity.height / 2
    }
}