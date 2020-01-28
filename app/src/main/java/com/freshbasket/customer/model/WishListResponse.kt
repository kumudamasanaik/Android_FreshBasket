package com.freshbasket.customer.model

data class WishListResponse(
        val result: ArrayList<Product>,
        val summary: CartSummary
):CommonRes()