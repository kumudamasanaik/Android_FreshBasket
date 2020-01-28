package com.freshbasket.customer.screens.productlist.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Category
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.catgory_dialog_adapter_list_item.*

class ProductListViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is Category) {

            if (!item.name.isNullOrEmpty())
                tv_category_list.text = item.name!!


            itemView.setOnClickListener {

                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType)
            }

        }
    }


}
