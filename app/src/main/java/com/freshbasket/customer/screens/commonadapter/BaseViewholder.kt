package com.freshbasket.customer.screens.commonadapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewholder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(context: Context, item: Any, pos: Int = 0)
}
