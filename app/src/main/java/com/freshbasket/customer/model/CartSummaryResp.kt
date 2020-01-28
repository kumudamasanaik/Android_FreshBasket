package com.freshbasket.customer.model

import com.freshbasket.customer.model.inputmodel.Address

data class CartSummaryResp(var address: Address?,
                           var cart: ArrayList<Product>?,
                           var orders: ArrayList<Orders>?,
                           var pay_options: ArrayList<PayOption>?,
                           var summary: CartSummary?) : CommonRes()