package com.freshbasket.customer.screens.navigation

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.freshbasket.customer.R
import com.freshbasket.customer.model.NavigationDrawerModel

class NavigationDrawerListAdapter(var itemList: ArrayList<NavigationDrawerModel> = ArrayList(), var context: Context) : BaseExpandableListAdapter() {

    fun addArrayList(itemList: ArrayList<NavigationDrawerModel>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun getGroupCount(): Int {
        return itemList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return itemList.get(groupPosition).subItemList?.size ?: 0
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var conView = convertView
        if (conView == null) {
            val inflaInflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            conView = inflaInflater.inflate(R.layout.item_nav_parent, parent, false)
        }
        conView?.setPadding(10, 0, 0, 0)
        val tvTitle = conView?.findViewById(R.id.tv_title) as AppCompatTextView
        val ivIcon = conView.findViewById(R.id.iv_nav_icon) as AppCompatImageView
        val ivExapand = conView.findViewById(R.id.iv_nav_expand) as AppCompatImageView
        val iv_shop_by_cat_move = conView.findViewById(R.id.iv_shop_by_cat_move) as AppCompatImageView


        tvTitle.text = getGroup(groupPosition).title
        ivIcon.setImageDrawable(getGroup(groupPosition).icon)

        if (!getGroup(groupPosition).title.isNullOrEmpty() && getGroup(groupPosition).title!!.contentEquals(context.getString(R.string.shop_by_category)))
            iv_shop_by_cat_move.visibility = View.VISIBLE
        else
            iv_shop_by_cat_move.visibility = View.GONE

        if (isExpanded) {
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            ivExapand.setImageResource(R.drawable.ic_nav_remove)
        } else {
            ivExapand.setImageResource(R.drawable.ic_nav_add)
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.md_grey_600))
        }



        if (getGroup(groupPosition).subItemList == null)
            ivExapand.visibility = View.GONE
        else
            ivExapand.visibility = View.VISIBLE
        return conView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {

        var conView = convertView
        if (conView == null) {
            var inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            conView = inflater.inflate(R.layout.item_nav_child, parent, false)
        }

        conView?.setPadding(40, 0, 0, 0)
        var tvTitle = conView?.findViewById(R.id.tv_title) as AppCompatTextView

        tvTitle.text = getChild(groupPosition, childPosition)

        return conView
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String? = itemList.get(groupPosition).subItemList?.get(childPosition)
    override fun getGroup(groupPosition: Int): NavigationDrawerModel = itemList.get(groupPosition)
    override fun getChildId(groupPosition: Int, childPosition: Int): Long = groupPosition.toLong()
    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()
    override fun hasStableIds(): Boolean = false
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true
}