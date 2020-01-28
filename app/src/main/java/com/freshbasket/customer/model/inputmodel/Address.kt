package com.freshbasket.customer.model.inputmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
        var id_address: String? = null,
        var _id: String? = null,
        val id_customer: String?,
        val op: String?,
        val address_nickname: String?,
        val first_name: String?,
        val last_name: String?,
        val house_no: String?,
        val residential_complex: String?,
        val locality: String?,
        val area: String? = null,
        val pincode: String?,
        val street_name: String?,
        val landmark: String?,
        val selected: Int?,
        val lon: String?,
        val lat: String?,
        val alias: String? = null,
        val phone: String?,
        val type: String? = null
) : Parcelable {
    fun getAddressString(): String {
        return StringBuilder().append("#").append(house_no).append(","+" ").append(area).append(pincode).toString()
    }
}