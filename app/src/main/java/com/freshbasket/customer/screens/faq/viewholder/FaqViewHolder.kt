package com.freshbasket.customer.screens.faq.viewholder

import android.content.Context
import android.view.View
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Faq
import com.freshbasket.customer.screens.commonadapter.BaseViewholder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.items_faq_details.*

class FaqViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {

        if (item is Faq) {
            if (!item.question.isNullOrEmpty()) {
                tv_faq_text_num.text = (pos + 1).toString().plus(".")
                tv_faq_text.text = item.question
            }


            if (!item.answer.isNullOrEmpty())
                tv_faq_subtxt.text = item.answer
        }
    }
}