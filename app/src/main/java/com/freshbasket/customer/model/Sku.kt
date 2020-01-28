package com.freshbasket.customer.model

data class Sku(
        val _id: String? = null,
        val box_color: String? = null,
        val filter_color: String? = null,
        val id_product: String? = null,
        val min_quantity: String? = null,
        val mrp: String? = null,
        var mycart: String? = null,
        val offer: String? = null,
        val realization: String? = null,
        val selling_price: Int? = 0,
        val size: String? = null,
        val size_measuring_unit: String? = null,
        val sku: String? = null,
        val stock: String? = null,
        val tax: String? = null,
        var tempMyCart: Int = -1
)