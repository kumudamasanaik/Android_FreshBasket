package com.freshbasket.customer.screens.offers.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.OfferResp
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.partial_offers_list_item.*

class OffersViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {
        item.apply {
            if (item is OfferResp.SkuDetail) {
                if (!item.title.isNullOrEmpty())
                    product_name.text = item.title

                if (!item.offer_price.isNullOrEmpty()) {
                    var price = CommonUtils.getRupeesSymbol(context, item.offer_price!!)
                    tv_product_price.text = price
                }

                if (!item.pic!![0].pic.isNullOrEmpty())
                    ImageLoader.setImage(image_product, item.pic!![0].pic!!)

                if (!item.description.isNullOrEmpty())
                    tv_details.text = item.description


                image_product.setOnClickListener {
                    if (adapterClickListener != null)
                        adapterClickListener!!.onclick(item, adapterPosition, adapterType, Constants.TYPE_OFFERS_ADAPTER)
                }
            }
        }
    }
}