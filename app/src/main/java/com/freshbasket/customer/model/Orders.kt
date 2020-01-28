package com.freshbasket.customer.model

data class Orders(
        var added: String?,
        var delivery_charge: String?,
        var delivery_message: String?,
        var delivery_type: String?,
        var deliveryslot: ArrayList<Deliveryslot>?,
        var grand_total: Long?,
        var id_customer: Int?,
        var order_id: Int?,
        var order_no: String?,
        var order_status: String?,
        var show_slot: String?,
        var total_discount: Int?,
        var total_items: Int?,
        var total_mrp: Long?,
        var total_promo_code_discount: Int?,
        var total_selling_discount: Long?,
        var total_selling_price: Long?

















)