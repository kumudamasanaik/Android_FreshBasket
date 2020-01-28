package com.freshbasket.customer.screens.notification.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Notification
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_notification_list_items.*

class NotificationViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {
        item.apply {
            if (item is Notification) {
                if (!item.date_time.isNullOrEmpty())
                    tv_delivery_date.text = item.date_time

                if (!item.message.isNullOrEmpty())
                    tv_delivery_item.text = item.message


                itemView.setOnClickListener {
                    if (adapterClickListener != null)
                        adapterClickListener!!.onclick(item, adapterPosition, adapterType, Constants.TYPE_ORDER_DETAIL_ADAPTER)
                }
            }
        }

    }
}