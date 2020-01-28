package com.freshbasket.customer.screens.orderdetails.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Cart
import com.freshbasket.customer.model.Product
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_order_list.*

class MyOrderDetailsViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        item.apply {
            if (item is Cart) {

                if (!item.product_name.isNullOrEmpty())
                    product_name.text = item.product_name

                if (!item.size.isNullOrEmpty())
                    tv_weight.text = item.size

                if (!item.quantity.isNullOrEmpty())
                    tv_quatity_count.text = item.quantity

                if (!item.mrp.isNullOrEmpty())
                // tv_rupees.text = item.mrp
                    tv_rupees.text = CommonUtils.getRupeesSymbol(context, item.mrp!!)

                if (!item.product_pic.isNullOrEmpty())
                    ImageLoader.setImage(image_product, item.product_pic!!)



                itemView.setOnClickListener {
                    if (adapterClickListener != null)
                        adapterClickListener!!.onclick(item, adapterPosition, adapterType, op = Constants.TYPE_ORDER_DETAIL_ADAPTER)
                }

                btn_return.setOnClickListener {
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, op = Constants.ORDER_RETURNED)
                }
            } else if (item is Product) {
                if (!item.name.isNullOrEmpty())
                    product_name.text = item.name

                if (!item.sku!![0]?.size.isNullOrEmpty())
                    tv_weight.text = item.sku[0]?.size

                if (!item.sku[0]?.mycart.isNullOrEmpty())
                    tv_quatity_count.text = item.sku[0]?.mycart

                if (!item.sku[0]?.mrp.isNullOrEmpty())
                // tv_rupees.text = item.mrp
                    tv_rupees.text = CommonUtils.getRupeesSymbol(context, item.sku[0]?.mrp!!)

                if (!item.image.isNullOrEmpty())
                    ImageLoader.setImage(image_product, item.image!!)

            }
        }


    }
}