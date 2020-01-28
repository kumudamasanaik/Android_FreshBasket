package com.freshbasket.customer.model

data class CartSummary(
        val cart_count: String?,
        val delivery_charge: String,
        val grand_total: String,
        val mrp: String,
        val realization: String,
        val selling_price: String
)