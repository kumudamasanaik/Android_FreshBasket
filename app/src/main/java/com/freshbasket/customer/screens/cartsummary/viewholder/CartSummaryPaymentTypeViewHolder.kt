package com.freshbasket.customer.screens.cartsummary.viewholder

import android.content.Context
import android.provider.SyncStateContract
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Deliveryslot
import com.freshbasket.customer.model.PayOption
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.Validation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partial_layout_cash_on_delivery.*

class CartSummaryPaymentTypeViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is PayOption) {
            if (Validation.isValidObject(item) && Validation.isValidString(item.type))
                tv_payment_type.text = item.type!!.toUpperCase()


            if ( Validation.isValidString(item.type) && item.type!!.contentEquals(Constants.PAYMENT_TPE_ONLINE))
                iv_payment_type_image.setImageResource(R.drawable.ic_payment_netbanking)

            if ( Validation.isValidString(item.type) && item.type!!.contentEquals(Constants.PAYMENT_TPE_COD))
                iv_payment_type_image.setImageResource(R.drawable.ic_payment_cash_on_delivery)


            if (Validation.isValidObject(item) && Validation.isValidString(item.type))
                tv_payment_type.text = item.type!!.toUpperCase()

            itemView.setOnClickListener {
                adapterClickListener?.apply {
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.PAY_TYPE)
                }

            }

        }
    }


}