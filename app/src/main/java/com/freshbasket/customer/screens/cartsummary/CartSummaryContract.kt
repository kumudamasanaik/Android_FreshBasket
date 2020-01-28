package com.freshbasket.customer.screens.cartsummary

import com.freshbasket.customer.model.CartSummaryResp
import com.freshbasket.customer.model.CheckOutResp
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.google.gson.JsonArray

interface CartSummaryContract {

    interface View : BaseView {
        fun callCartSummaryApi()
        fun setCartSummaryApiResp(cartsummaryres: CartSummaryResp)
        fun setupCheckoutApi()
        fun setcheckoutApiResp(checkoutResp: CheckOutResp)


    }

    interface Presenter : BasePresenter<View> {
        fun callCartSummaryApi()
        fun callCheckOutApi(jsonArray: JsonArray)

    }
}