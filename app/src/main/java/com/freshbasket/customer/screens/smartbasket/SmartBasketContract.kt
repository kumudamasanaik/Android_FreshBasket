package com.freshbasket.customer.screens.smartbasket

import com.freshbasket.customer.model.ComboDetailResp
import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface SmartBasketContract {
    interface View : BaseView {
        fun callSmartBasket()
        fun setSmartBasketResp(res: ComboDetailResp)
    }

    interface Presenter : BasePresenter<View> {
        fun callSmartBasketApi(cat_id :String)
    }
}