package com.freshbasket.customer.screens.shopbycategory.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.SubCategory
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_shop_by_category_child_list_item.*

class ShopByChildCateViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {

        if (item is SubCategory) {

            if (!item.name.isNullOrEmpty())
                tv_child_cat_name.text = item.name!!

            itemView.setOnClickListener {

                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType)
            }


        }


    }
}
