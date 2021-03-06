package com.freshbasket.customer.model

import com.freshbasket.customer.model.inputmodel.Address

data class CheckOutResp(var orders: List<Order?>?):CommonRes() {
    data class Order(
            var address: Address?,
            var cart: List<Cart?>?,
            var checkoutOrders: CheckoutOrders?
    ) {
        data class CheckoutOrders(
                var _id: String?,
                var ack_message: String?,
                var ack_status_code: String?,
                var added: String?,
                var assign_delivery: String?,
                var cancelled_on: Any?,
                var coupon: String?,
                var delivered_on: Any?,
                var delivery_charge: String?,
                var delivery_no: String?,
                var delivery_slot: String?,
                var delivery_slot_id: String?,
                var delivery_slot_name: String?,
                var delivery_slot_time: String?,
                var delivery_type: String?,
                var deliverypay_option: String?,
                var deliverypay_type: String?,
                var email: String?,
                var email_sent: String?,
                var email_sent_on: Any?,
                var first_name: String?,
                var giftcard_discount: String?,
                var grand_total: String?,
                var id_coupon: String?,
                var id_customer: String?,
                var id_giftcard: String?,
                var id_warehouse: String?,
                var invoice_date: String?,
                var invoice_no: String?,
                var invoice_url: String?,
                var is_first: String?,
                var message: String?,
                var mobile: Any?,
                var order_no: String?,
                var order_return_status: String?,
                var order_status: String?,
                var pause_time: String?,
                var pay_option: String?,
                var pay_response: String?,
                var pay_status: String?,
                var pay_type: String?,
                var pick_end_time: String?,
                var pick_start_time: String?,
                var picked_by: String?,
                var picked_grand_total: String?,
                var referral_amount: String?,
                var referral_code: String?,
                var return_deliveryperson_id: String?,
                var returned_items: String?,
                var returned_on: Any?,
                var returned_reason: String?,
                var returnedpay_type: String?,
                var total_discount: String?,
                var total_items: String?,
                var total_mrp: String?,
                var total_paid: String?,
                var total_promo_code_discount: String?,
                var total_returned_amount: String?,
                var total_selling_discount: String?,
                var total_selling_price: String?,
                var updated: String?,
                var wallet_amt_applied: String?
        )

        data class Cart(
                var _id: String?,
                var active: String?,
                var added: String?,
                var box_color: Any?,
                var brand_en: String?,
                var case_quantity: String?,
                var cessperc: String?,
                var cessperpc: String?,
                var cgst: String?,
                var check_expired: String?,
                var dispatch_quantity: String?,
                var ean_codes: String?,
                var express: String?,
                var fill_rate: String?,
                var filter_color: Any?,
                var group_id: Any?,
                var gst: String?,
                var hsn_codes: String?,
                var icm_name: String?,
                var id_order: String?,
                var id_product: String?,
                var id_sku: String?,
                var igst: String?,
                var max_quantity: String?,
                var mbq: String?,
                var min_quantity: String?,
                var mrp: String?,
                var ooos: String?,
                var order_part_type: String?,
                var pack_size: String?,
                var picked_quantity: String?,
                var product_name: String?,
                var product_pic: String?,
                var product_size: String?,
                var quantity: String?,
                var rack_location: String?,
                var realization: Any?,
                var returned_quantity: String?,
                var selling_price: String?,
                var sgst: String?,
                var size: String?,
                var size_measuring_unit: Any?,
                var sku: String?,
                var sold: String?,
                var stock: String?,
                var sub_type: String?,
                var supplier_price: String?,
                var tax: String?,
                var taxtype: String?,
                var threshold_margin_value: String?,
                var updated: String?,
                var updated_quantity: String?,
                var variable_weight: String?,
                var weight_in_gms: String?
        )

    }
}