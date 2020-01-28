package com.freshbasket.customer.screens.productdetail.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.ProductDetailsResp
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partial_information_list_item.*

class ProductDetailsViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is ProductDetailsResp.Product.Spec)
            tv_info.text = item.value
    }
}