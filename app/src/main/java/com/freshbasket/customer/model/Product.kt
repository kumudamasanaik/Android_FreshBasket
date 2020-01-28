package com.freshbasket.customer.model

import com.freshbasket.customer.model.inputmodel.ModifyCartIp

data class Product(
        val _id: String? = null,
        val actual_price: Int? = null,
        val article_no: String?  = null,
        val brand: String?  = null,
        val category1: String?  = null,
        val category1_id: String?  = null,
        val category2: String?  = null,
        val category2_id: String?  = null,
        val description: String?  = null,
        val id_category: String?  = null,
        val mrp: Int?  = null,
        val name: String?  = null,
        val pic: ArrayList<Pic>?  = null,
        val selling_price: Int?  = null,
        val sku: ArrayList<Sku>?  = null,
        val tags: String?  = null,
        var wishlist: Int?  = null,

        /*THESE PARAMETER ONLY AVAILIABLE CART API*/
        val express: String?  = null,
        val image: String?  = null,
        val my_express: String?  = null,

        /*THESE ARE EXTRA PARMETER OUR LOGIC IMPLEMENTATION*/
        var selSku: Sku? = null,
        var modifyCartIp: ModifyCartIp? = null,
        var prodPos: Int = -1,

        var tempWishlist: Int = -1

) {
    fun getSelectedSku(): Sku? {
        return if (selSku == null && sku!!.isNotEmpty())
            sku[0]
        else
            selSku

    }


    fun isWishListEnabled(): Boolean {
        return wishlist == 1
    }


}