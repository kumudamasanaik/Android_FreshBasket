package com.freshbasket.customer.model

data class ComboDetailResp(
        val combo: ArrayList<Combo>?
) :CommonRes(){
    data class Combo(
            val _id: String?,
            val name: String?,
            val article_no: String?,
            val description: String?,
            val tags: String?,
            val brand: String?,
            val pic: ArrayList<Pic>?,
            val wishlist: Int?,
            val sku: ArrayList<Sku>?,
            val products: ArrayList<Product>?
    ) {
        data class Product(
                val _id: String?,
                val id_combo: String?,
                val product_name: String?,
                val quantity: String?,
                val size: String?,
                val pic: String?,
                val mrp: String?,
                val selling_price: String?,
                val updated: String?
        )

        data class Pic(
                val _id: String?,
                val pic: String?,
                val pic_thumb: String?,
                val id_product: String?,
                val updated: String?
        )

        data class Sku(
                val _id: String?,
                val id_product: String?,
                val sku: String?,
                val size: String?,
                val size_measuring_unit: Any?,
                val sub_type: String?,
                val box_color: Any?,
                val filter_color: Any?,
                val group_id: Any?,
                val ean_codes: String?,
                val hsn_codes: String?,
                val threshold_margin_value: String?,
                val case_quantity: String?,
                val check_expired: String?,
                val supplier_price: String?,
                val pack_size: String?,
                val order_part_type: String?,
                val weight_in_gms: String?,
                val variable_weight: String?,
                val rack_location: String?,
                val icm_name: String?,
                val fill_rate: String?,
                val mrp: String?,
                val realization: String?,
                val selling_price: String?,
                val tax: String?,
                val gst: String?,
                val cgst: String?,
                val sgst: String?,
                val igst: String?,
                val taxtype: String?,
                val min_quantity: String?,
                val mbq: String?,
                val max_quantity: String?,
                val ooos: String?,
                val stock: String?,
                val cessperc: String?,
                val cessperpc: String?,
                val express: String?,
                val active: String?,
                val sold: String?,
                val updated: String?,
                val quantity: String?,
                val mycart: String?
        )
    }
}