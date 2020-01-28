package com.freshbasket.customer.screens.offersdetails.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.OrderDetailsResp
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partial_offers_recyclerview_item.*

class OffersDetailsViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        item.apply {
            if (item is OrderDetailsResp.BuyoneGetone.Product) {
                if (!item.name.isNullOrEmpty())
                    tv_item_name.text = item.name

                if (!item.selling_price.isNullOrEmpty()) {
                    var price = CommonUtils.getRupeesSymbol(context, item.selling_price!!)
                    tv_item_price.text = price
                }

                if (!item.quantity.isNullOrEmpty())
                    tv_mrp_price.text = item.quantity

                if (!item.pic.isNullOrEmpty())
                    ImageLoader.setImage(image_product, item.pic!!)
            }
        }
    }
}