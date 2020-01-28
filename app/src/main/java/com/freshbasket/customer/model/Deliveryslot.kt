package com.freshbasket.customer.model

data class Deliveryslot(
        var date: String?,
        var times: ArrayList<Time>?
) {
    data class Time(
            var name: String?,
            var slot_id: String?,
            var time: String?
    )
}