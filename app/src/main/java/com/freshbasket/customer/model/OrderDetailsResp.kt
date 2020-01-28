package com.freshbasket.customer.model

data class OrderDetailsResp(
        val buyone_getone: ArrayList<BuyoneGetone>?
):CommonRes() {
    data class BuyoneGetone(
            val _id: String?,
            val title: String?,
            val description: String?,
            val added: String?,
            val updated: String?,
            val pic: ArrayList<Pic>?,
            val products: ArrayList<Product>?
    ) {
        data class Product(
                val _id: String?,
                val p_id: String?,
                val id_product: String?,
                val pic: String?,
                val id_sku: String?,
                val price: String?,
                val quantity: String?,
                val is_buyone: String?,
                val selling_price: String?,
                val name: String?,
                val article_no: String?,
                val description: String?,
                val tags: String?,
                val brand: String?,
                val id_category: String?,
                val category2_id: String?,
                val category1_id: String?,
                val category2: String?,
                val category1: String?
        )

        data class Pic(
                val _id: String?,
                val pic: String?,
                val id_parent: String?,
                val updated: String?
        )
    }
}