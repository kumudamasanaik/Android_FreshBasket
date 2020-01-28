package com.freshbasket.customer.model

data class OfferResp(
        val sku_details: ArrayList<SkuDetail>?
):CommonRes() {
    data class SkuDetail(
            val _id: String?,
            val title: String?,
            val description: String?,
            val offer_price: String?,
            val pic: ArrayList<Pic>?
    ) {
        data class Pic(
                val _id: String?,
                val pic: String?,
                val id_parent: String?,
                val updated: String?
        )
    }
}