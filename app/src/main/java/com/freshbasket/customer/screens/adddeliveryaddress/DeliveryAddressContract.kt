package com.freshbasket.customer.screens.adddeliveryaddress

import com.freshbasket.customer.model.DeliveryAddRes
import com.freshbasket.customer.model.LocalityRes
import com.freshbasket.customer.model.inputmodel.Address
import com.freshbasket.customer.screens.BaseView

interface DeliveryAddressContract {
    interface View : BaseView {
        fun getIntentData()
        fun setUpAddressData()
        fun getlocality()
        fun addDeliveryAddress()
        fun callUpdateAddress()

        fun setLocalityRes(res: LocalityRes)
        fun setAddDeliveryAddressRes(res: DeliveryAddRes)
        fun showDeliveryAddressValidateErrorMsg(msg: String)
        fun setupdateAddressApiRes(res: DeliveryAddRes)

    }

    interface Presenter {
        fun validate(input: Address): Boolean
        fun callgetLocality(op: String, city: String)
        fun callAddDeliveryAddress(input: Address)
        fun callUpdateAddressApi(input: Address)

    }
}