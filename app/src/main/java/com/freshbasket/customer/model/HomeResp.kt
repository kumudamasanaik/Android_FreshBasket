package com.freshbasket.customer.model


data class HomeResp(
        val result: Home,
        val summary: CartSummary

) : CommonRes()