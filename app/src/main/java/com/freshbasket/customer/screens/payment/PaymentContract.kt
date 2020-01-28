package com.freshbasket.customer.screens.payment

import com.freshbasket.customer.model.CheckOutResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.google.gson.JsonArray

interface PaymentContract {
    interface View : BaseView {
        fun setupCheckoutApi()
        fun setcheckoutApiResp(checkoutResp: CheckOutResp)
       // fun setupPaymentScreen()


    }

    interface Presenter : BasePresenter<View> {
        fun callCheckOutApi(jsonArray: JsonArray)

    }
}