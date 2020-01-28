package com.freshbasket.customer.screens.coupon.viewholder

import android.content.Context
import android.provider.SyncStateContract
import android.view.View
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.CouponResData
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.Validation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.items_coupon_list.*

class CouponViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {
    override fun bind(context: Context, item: Any, pos: Int) {

        if(item is CouponResData){

            if (Validation.isValidObject(item) && Validation.isValidString(item.title))
                tv_coupon_title.text = item.title

            if (Validation.isValidObject(item) && Validation.isValidString(item.conditions))
                tv_condition.text = item.conditions

            if (Validation.isValidObject(item) && Validation.isValidString(item.validity_end))
                tv_end_date.text = item.validity_end
        }

        tv_apply_coupon.setOnClickListener {

            adapterClickListener!!.onclick(item = item, pos = adapterPosition, op = Constants.APPLY_PROMO_CODE)
        }

    }
}