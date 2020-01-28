package com.freshbasket.customer.customviews.decoration

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.freshbasket.customer.R

class InsetDecoration(context: Context, dimenId: Int = R.dimen.card_insets, private val allSides: Boolean = false) : RecyclerView.ItemDecoration() {


    private var mInsets = dimenId

    init {
        mInsets = context.resources.getDimensionPixelSize(dimenId)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        //We can supply forced insets for each item view here in the Rect
        if (!allSides)
            outRect.set(1, mInsets, 1, mInsets)
        else
            outRect.set(mInsets, mInsets, mInsets, mInsets)
    }


}
