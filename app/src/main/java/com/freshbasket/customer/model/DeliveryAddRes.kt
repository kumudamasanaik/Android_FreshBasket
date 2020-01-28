package com.freshbasket.customer.model

import com.freshbasket.customer.model.inputmodel.Address

data class DeliveryAddRes (
        val customer: Customer,
        val result: ArrayList<Address>
) : CommonRes()