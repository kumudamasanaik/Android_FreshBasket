package com.freshbasket.customer.screens.shopbycategory.viewholder

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.freshbasket.customer.R
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.SubCategory
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import com.freshbasket.customer.util.ImageLoader
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_shop_by_category_list_item.*
import kotlinx.android.synthetic.main.partialy_shop_by_category_list_item.view.*

class ShopByCateViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {

        if (item is SubCategory) {
            if (!item.pic.isNullOrEmpty())
                ImageLoader.setImage(itemView.iv_fruits, item.pic!!)

            if (!item.name.isNullOrEmpty())
                tv_cate_name.text = item.name!!

            if (!item.description.isNullOrEmpty())
                tv_item_description.text = item.description!!

            if (item.subcategory!!.isEmpty()) {
                iv_arrow.visibility = View.GONE
                tv_item_description.visibility = View.GONE
            } else {
                rv_sub_category.visibility = View.GONE
                itemView.setOnClickListener {

                    if (item.isHavingSubList() && rv_sub_category.visibility == View.GONE) {

    
                        iv_arrow.setImageResource(R.drawable.ic_left_arrow_gray)
                        rv_sub_category.visibility = View.VISIBLE
                        rv_sub_category.apply {

                            val shopbycategoryAdpater = BaseRecAdapter(getContext(), R.layout.partialy_shop_by_category_child_list_item, adapterType = item._id!!, adapterClickListener = adapterClickListener)
                            val linearLayout = LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false)
                            layoutManager = linearLayout
                            adapter = shopbycategoryAdpater
                            isNestedScrollingEnabled = false
                            shopbycategoryAdpater.addList(item.subcategory!!)
                        }

                     /*   iv_fruits.setOnClickListener {

                            adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType)
                        }

                        tv_cate_name.setOnClickListener {

                            adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType)
                        }*/

                        // adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType)

                    } else {
                        iv_arrow.setImageResource(R.drawable.ic_right_arrow)
                        rv_sub_category.visibility = View.GONE
                    }
                }
            }
        }
    }
}