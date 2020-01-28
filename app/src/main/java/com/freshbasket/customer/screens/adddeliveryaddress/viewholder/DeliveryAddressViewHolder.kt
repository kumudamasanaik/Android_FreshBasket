package com.freshbasket.customer.screens.adddeliveryaddress.viewholder

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Locality
import com.freshbasket.customer.model.inputmodel.Address
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.items_delivery_address.*
import kotlinx.android.synthetic.main.locality_dialog_adapter_list_item.*

class DeliveryAddressViewHolderr(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {
        if (item is Locality) {

            if (!item.city.isNullOrEmpty())
                tv_locality_list.text = item.city


            itemView.setOnClickListener {
                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType)
            }

        }


        if (Validation.isValidObject(item) && item is Address) {
            if (!item.first_name.isNullOrEmpty())
                tv_nameadd.text = item.first_name

            tv_address.text = item.getAddressString()

            if (item.selected == 1) {
                rb_image.setImageResource(R.drawable.ic_radio_button_on)
                ll_address_selection.setBackgroundColor(ContextCompat.getColor(context, R.color.color_gray))
                CommonUtils.saveDeliveryAddress(item.area)
                tv_default.visibility = View.GONE
            } else {
                ll_address_selection.setBackgroundColor(ContextCompat.getColor(context, R.color.app_txt_white))
                rb_image.setImageResource(R.drawable.ic_radio_button_off)
            }



            rb_image.setOnClickListener {
                if (Validation.isValidObject(adapterClickListener))
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.LIST_ADDRESS)
            }

            tv_default.setOnClickListener {
                if (Validation.isValidObject(adapterClickListener))
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.LIST_ADDRESS)
            }

            iv_exit.setOnClickListener {
                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.DELETE)

            }
            tv_edit.setOnClickListener {
                if (Validation.isValidObject(adapterClickListener))
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.EDIT)
            }
        }
    }
}