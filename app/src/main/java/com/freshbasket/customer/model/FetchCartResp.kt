package com.freshbasket.customer.model

class FetchCartResp(
        val cart: ArrayList<Product>?,
        val summary: CartSummary?
) : CommonRes()