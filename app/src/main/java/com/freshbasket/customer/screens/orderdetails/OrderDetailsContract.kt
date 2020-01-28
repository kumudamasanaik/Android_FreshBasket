package com.freshbasket.customer.screens.orderdetails

import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.Order
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.google.gson.JsonArray

interface OrderDetailsContract {
    interface View : BaseView {
        fun callOrderDetails()
        fun setOrderDetailsResp(res: Order)
        fun setReturnApiRes(res: CommonRes)
    }

    interface Presenter : BasePresenter<View> {
        fun callMyOrderDetailsApi(order_no: String)
        fun callReturnApi(id:String,order_id:String,return_reason:String,jsonArray: JsonArray)


    }
}