package com.freshbasket.customer.model

data class CouponRes(val id_customer: String?, val result: ArrayList<CouponResData>?) : CommonRes()

data class CouponResData(
        val _id: String? = null,
        val title: String? = null,
        val promo_code: String? = null,
        val discount_type: String? = null,
        val validity_start: String? = null,
        val validity_end: String? = null,
        val order_value: String? = null,
        val conditions: String? = null,
        val status: String? = null,
        val amount: String? = null,
        val updated: String? = null,
        val coupon_code: String? = null,
        val discount: String? = null)
