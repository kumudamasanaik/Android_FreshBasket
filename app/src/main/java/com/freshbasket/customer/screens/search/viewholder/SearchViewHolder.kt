package com.freshbasket.customer.screens.search.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Product
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_cart_list_item.view.*

class SearchViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {

        if (item is Product) {
            ImageLoader.setImage(itemView.image_product, item.pic!![0].pic!!)

          //  product_name

            itemView.setOnClickListener {
                adapterClickListener!!.onclick(item, adapterPosition, adapterType, Constants.OP_PROD_DETAILS)
            }
        }
    }
}