package com.freshbasket.customer.screens.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Category
import com.freshbasket.customer.model.Deal
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_main_category.*
import kotlinx.android.synthetic.main.item_main_category.view.*

class MaincatViewholder(override val containerView: View, val adapterType: String = "common", val adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {

        if (item is Category) {
            if (!item.pic.isNullOrEmpty())
                ImageLoader.setImage(itemView.iv_product_image, item.pic!!)

            if (!item.name.isNullOrEmpty())
                tv_cat_name.text = item.name!!


            iv_product_image.setOnClickListener {
                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType)
            }

            itemView.setOnClickListener {
                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType)
            }
        }

        /*FOR DEALS*/
        if (item is Deal) {
            if (!item.pic.isNullOrEmpty())
                ImageLoader.setImage(itemView.iv_product_image, item.pic!!)

            if (!item.name.isNullOrEmpty())
                tv_cat_name.text = item.name!!

            iv_product_image.setOnClickListener {
                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType)
            }

            itemView.setOnClickListener {
                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType)
            }
        }


    }
}