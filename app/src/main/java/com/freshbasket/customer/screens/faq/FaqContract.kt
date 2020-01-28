package com.freshbasket.customer.screens.faq

import com.freshbasket.customer.model.FaqRes
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface FaqContract {
    interface View : BaseView {
        fun callFaq()
        fun setFaqResp(res: FaqRes)
    }

    interface Presenter : BasePresenter<View> {
        fun callFAQApi(op:String,table:String)
    }
}