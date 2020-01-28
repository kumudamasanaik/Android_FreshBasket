package com.freshbasket.customer.util

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.freshbasket.customer.R
import com.freshbasket.customer.customviews.decoration.InsetDecoration

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.makeVisible(isVisible: Boolean) {
    if (isVisible)
        this.visibility = View.VISIBLE
    else
        this.visibility = View.GONE
}

fun RecyclerView.addDivider(context: Context, lManager: LinearLayoutManager, divSize: Int = R.drawable.divider_2dp) {
    val divDec = DividerItemDecoration(context, lManager.orientation)
    divDec.setDrawable(ResourcesCompat.getDrawable(context.resources, divSize, null)!!)
    this.addItemDecoration(divDec)
}

fun RecyclerView.addSpace(allSides: Boolean = false) = addItemDecoration(InsetDecoration(context, R.dimen.card_4dp, allSides = allSides))

fun View.rotateImg(angle: Int = 0, duration: Long = 10) {
    animate().setDuration(duration).rotationBy((angle).toFloat()).start()
}