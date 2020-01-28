package com.freshbasket.customer.model

import com.freshbasket.customer.model.inputmodel.Address

data class Order(
        val address: Address,
        val cart: ArrayList<Cart>?,
        val order: OrderSummary
)