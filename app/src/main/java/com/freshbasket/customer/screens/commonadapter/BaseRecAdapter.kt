package com.freshbasket.customer.screens.commonadapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.freshbasket.customer.R
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.screens.adddeliveryaddress.viewholder.DeliveryAddressViewHolderr
import com.freshbasket.customer.screens.cartsummary.viewholder.CartSummaryPaymentTypeViewHolder
import com.freshbasket.customer.screens.coupon.viewholder.CouponViewHolder
import com.freshbasket.customer.screens.faq.viewholder.FaqViewHolder
import com.freshbasket.customer.screens.filtersactivity.refinedfragment.RefineTypeViewHolder
import com.freshbasket.customer.screens.myorder.viewholder.MyOrderViewHolder
import com.freshbasket.customer.screens.notification.viewholder.NotificationViewHolder
import com.freshbasket.customer.screens.offers.viewholder.OffersViewHolder
import com.freshbasket.customer.screens.offersdetails.viewholder.OffersDetailsViewHolder
import com.freshbasket.customer.screens.orderdetails.viewholder.MyOrderDetailsViewHolder
import com.freshbasket.customer.screens.productdetail.viewholder.ProductDetailsViewHolder
import com.freshbasket.customer.screens.productlist.viewholder.ProductListViewHolder
import com.freshbasket.customer.screens.search.viewholder.SearchViewHolder
import com.freshbasket.customer.screens.shopbycategory.viewholder.ShopByCateViewHolder
import com.freshbasket.customer.screens.shopbycategory.viewholder.ShopByChildCateViewHolder
import com.freshbasket.customer.screens.smartbasket.viewholder.SmartBasketViewHolder
import com.freshbasket.customer.screens.viewholder.MaincatViewholder
import com.freshbasket.customer.util.inflate
import com.freshbasket.customer.util.withNotNullNorEmpty

class BaseRecAdapter(var context: Context, var type: Int, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : RecyclerView.Adapter<BaseViewholder>() {

    var list: ArrayList<*>? = null


    fun addList(list: ArrayList<*>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewholder {
        val view = parent.inflate(type)
        lateinit var holder: BaseViewholder
        when (type) {
            R.layout.item_main_category -> holder = MaincatViewholder(view, adapterType, adapterClickListener)

            R.layout.partialy_cart_list_item -> holder = MaincatViewholder(view, adapterType, adapterClickListener)

            R.layout.partialy_shop_by_category_list_item -> holder = ShopByCateViewHolder(view, adapterType, adapterClickListener)

            R.layout.partialy_notification_list_items -> holder = NotificationViewHolder(view, adapterType, adapterClickListener)

            R.layout.partialy_my_order_list_items -> holder = MyOrderViewHolder(view, adapterType, adapterClickListener)

            R.layout.items_check_out -> holder = MaincatViewholder(view, adapterType, adapterClickListener)

            R.layout.adapter_order_list -> holder = MyOrderDetailsViewHolder(view, adapterType, adapterClickListener)

            R.layout.items_faq_details -> holder = FaqViewHolder(view, adapterType, adapterClickListener)

            R.layout.items_delivery_address -> holder = DeliveryAddressViewHolderr(view, adapterType, adapterClickListener)

            R.layout.partialy_shop_by_category_child_list_item -> holder = ShopByChildCateViewHolder(view, adapterType, adapterClickListener)

            R.layout.catgory_dialog_adapter_list_item -> holder = ProductListViewHolder(view, adapterType, adapterClickListener)

            R.layout.partial_layout_cash_on_delivery -> holder = CartSummaryPaymentTypeViewHolder(view, adapterType, adapterClickListener)

            R.layout.locality_dialog_adapter_list_item -> holder = DeliveryAddressViewHolderr(view, adapterType, adapterClickListener)

            R.layout.filter_refined_adapter_headers_list_item -> holder = RefineTypeViewHolder(view, adapterType, adapterClickListener)

            R.layout.items_coupon_list -> holder = CouponViewHolder(view, adapterType, adapterClickListener)

            R.layout.partial_offers_list_item -> holder = OffersViewHolder(view, adapterType, adapterClickListener)

            R.layout.recyclerview_smart_basket_items -> holder = SmartBasketViewHolder(view, adapterType, adapterClickListener)

            R.layout.partial_information_list_item -> holder = ProductDetailsViewHolder(view, adapterType, adapterClickListener)

            R.layout.partial_offers_recyclerview_item -> holder = OffersDetailsViewHolder(view, adapterType, adapterClickListener)

        }
        return holder
    }

    override fun getItemCount(): Int {
        return if (list != null && list!!.size > 0) list!!.size else 6
    }

    override fun onBindViewHolder(holder: BaseViewholder, position: Int) {
        list.withNotNullNorEmpty {
            holder.bind(context, list!![position], position)
            return
        }
        holder.bind(context, holder, position)
    }

}