package com.freshbasket.customer.screens.filtersactivity.refinedfragment

import android.content.Context
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Refine
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.Validation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.filter_refined_adapter_headers_list_item.*

class RefineTypeViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {

        if (Validation.isValidObject(item) && Validation.isValidObject(adapterClickListener)) {
            if (item is Refine) {
                if (Validation.isValidString(item.name)) {
                    txt_item_list_title.text = item.name

                    itemView.setOnClickListener {
                        adapterClickListener?.onclick(item, adapterPosition, type = Constants.TYPE_RIFINED_LIST)
                        //txt_item_list_title.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
                    }
                }
                if (item.isSelected()) {
                    txt_item_list_title.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
                } else {
                    txt_item_list_title.setTextColor(context.resources.getColor(R.color.color_black))
                }


            }

        }
    }
}