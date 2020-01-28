package com.freshbasket.customer.screens.commonadapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.freshbasket.customer.R
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Banner
import com.freshbasket.customer.util.ImageLoader
import com.freshbasket.customer.util.Validation
import kotlinx.android.synthetic.main.item_banner_list_item.view.*
import java.util.*

class BannerAdapter(var context: Context, var type: Int, var clickListener: IAdapterClickListener? = null) : PagerAdapter() {

    var itemList: ArrayList<*>? = null
    lateinit var itemView: View

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return if (itemList != null && itemList!!.size > 0) itemList!!.size else 4
    }

    fun addList(itemList: ArrayList<*>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        itemList.apply {
            when (type) {
                R.layout.item_banner_list_item -> {
                    itemView = LayoutInflater.from(context).inflate(type, container, false)
                    if (Validation.isValidList(itemList)) {
                        val banner = this!![position] as Banner
                        if (!banner.pic.isNullOrBlank())
                            ImageLoader.setImage(itemView.banner_image, banner.pic!!)
                    }

                }
            }
        }

        container.addView(itemView)
        return itemView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}