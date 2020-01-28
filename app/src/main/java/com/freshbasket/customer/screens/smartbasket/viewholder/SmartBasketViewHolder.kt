package com.freshbasket.customer.screens.smartbasket.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.ComboDetailResp
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recyclerview_smart_basket_items.*

class SmartBasketViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {

        item.apply {
            if (item is ComboDetailResp.Combo.Product) {

                if (!item.quantity.isNullOrEmpty())
                    tv_products_qty.text = item.quantity

                if (!item.size.isNullOrEmpty())
                    tv_products_unit.text = item.size

                if (!item.product_name.isNullOrEmpty())
                    tv_product.text = item.product_name


            }

            tv_product.setOnClickListener {

                adapterClickListener!!.onclick(item = item, pos = adapterPosition, op = Constants.TYPE_SMART_BASKET_ADAPTER)
            }

        }
    }
}
