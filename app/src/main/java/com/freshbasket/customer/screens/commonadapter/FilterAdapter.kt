package com.freshbasket.customer.screens.commonadapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Sort
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.withNotNullNorEmpty

class FilterAdapter(var context: Context, var adapterType: String = "common",
                    var adapterClickListener: IAdapterClickListener? = null) : RecyclerView.Adapter<FilterAdapter.RefinedViewHolder>() {

    var list: ArrayList<Sort>? = null


    fun addList(list: ArrayList<Sort>) {
       // this.list?.clear()
        this.list = list

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefinedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.filter_refined_adapter_values_list_item, parent, false)
        return RefinedViewHolder(view, adapterClickListener, adapterType)
    }

    override fun getItemCount(): Int {
        return if (Validation.isValidList(list)) list!!.size else 0
    }

    override fun onBindViewHolder(holder: RefinedViewHolder, position: Int) {
        list.withNotNullNorEmpty {
            holder.bind(context, list!![position], position)
            return
        }
        holder.bind(context, holder, position)
    }

    inner class RefinedViewHolder(val containerView: View, var adapterClickListener: IAdapterClickListener?,
                                  var adapterType: String = "common") : RecyclerView.ViewHolder(containerView) {
        var cb_button = containerView.findViewById<CheckBox>(R.id.cb_selected)
        var item_name = containerView.findViewById<TextView>(R.id.txt_item_list_title)

        fun bind(context: Context, item: Any, pos: Int = 0) {

            cb_button.visibility = View.VISIBLE

            if (adapterType == Constants.SORT) {
                cb_button.setButtonDrawable(R.drawable.ic_radio_button_off)
            }
            if (item is Sort) {
                if (Validation.isValidString(item.name)) {
                    item_name.text = item.name
                    cb_button.isChecked = item.isChecked
                }

                cb_button.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (cb_button.isPressed) {
                        item.setChecked(isChecked)
                        adapterClickListener?.onclick(item, adapterPosition, Constants.SORT, adapterType)
                    }
                }
            }
        }
    }
}