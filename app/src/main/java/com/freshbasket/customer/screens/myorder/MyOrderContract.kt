package com.freshbasket.customer.screens.myorder

import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.MyOrderResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface MyOrderContract {
    interface View : BaseView {
        fun callMyOrder()
        fun setMyOrderResp(res: MyOrderResp)
        fun setCancelApiRes(res: CommonRes)

    }

    interface Presenter : BasePresenter<View> {
        fun callMyOrderApi()
        fun callCancelApi(id:String,order_no:String)
    }
}