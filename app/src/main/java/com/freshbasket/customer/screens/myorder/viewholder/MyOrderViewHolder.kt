package com.freshbasket.customer.screens.myorder.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.MyApplication.Companion.context
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Order
import com.freshbasket.customer.model.OrderSummary
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.CommonUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_my_order_list_items.*
import java.text.SimpleDateFormat

class MyOrderViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {
    private lateinit var orderData: OrderSummary
    private lateinit var strCurrentDate: String
    override fun bind(context: Context, item: Any, pos: Int) {
        item.apply {
            if (item is Order) {
                orderData = item.order

                if (!orderData.order_no.isNullOrEmpty())
                    tv_order_number.text = orderData.order_no


                if (!orderData.total_items.isNullOrEmpty())
                //       tv_items_count.text = orderData.total_items.plus(context.getString(R.string.items))
                    if (orderData.total_items != "1") {
                        tv_items_count.text = orderData.total_items + " Items"
                    } else {
                        tv_items_count.text = orderData.total_items + " Item"
                    }


                if (!orderData.order_status.isNullOrEmpty())
                    tv_del_status.text = orderData.order_status

                if (!orderData.grand_total.isNullOrEmpty())
                    tv_grand_price.text = CommonUtils.getRupeesSymbol(context, orderData.grand_total!!)

                if (!orderData.added.isNullOrEmpty())
                    strCurrentDate = orderData.added!!
                var format = SimpleDateFormat("yyyy-mmm-dd hh:mm:ss")
                val newDate = format.parse(strCurrentDate)

                format = SimpleDateFormat("MMM dd,yyyy, HH:mm aa")
                val date = format.format(newDate)
                tv_date.text = date

                updateOrderStatus(item.order.order_status!!)


                btn_cancel.setOnClickListener {
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, op = Constants.ORDER_CANCELLED)
                }

                btn_track_order.setOnClickListener {
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, op = Constants.TRACK_ORDER)
                }
            }
        }
    }

    private fun updateOrderStatus(order_status: String) {

        when (order_status) {
            Constants.CANCELLED -> {
                btn_cancel.visibility = View.GONE
                tv_del_status.setTextColor(context.resources.getColor(R.color.color_red))
            }
            Constants.PLACED -> {
                tv_del_status.setTextColor(context.resources.getColor(R.color.color_green))
            }
            Constants.PENDING -> {
                tv_del_status.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
            }
            else -> tv_del_status.setTextColor(context.resources.getColor(R.color.app_text_blck))

        }
    }
}