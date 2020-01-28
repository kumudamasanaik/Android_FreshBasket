package com.freshbasket.customer.screens.addressselection

import com.freshbasket.customer.model.DeliveryAddRes
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface SelectAddressContract {

    interface View : BaseView {

        fun callAddressListApi()
        fun setAddressListApiRes(res: DeliveryAddRes)
        fun setSelectAddressApiResp(selectAddResp: DeliveryAddRes)
        fun setdelAddressApiRes(res: DeliveryAddRes)


    }

    interface Presenter : BasePresenter<View> {
        fun callAddressListApi(op: String, customer_id: String)
        fun selectAddressApi(customer_id: String, op: String, address_id: String,selected:String)
       fun deleteAddressApi(op:String, address_id: String, customer_id:String)

    }

}