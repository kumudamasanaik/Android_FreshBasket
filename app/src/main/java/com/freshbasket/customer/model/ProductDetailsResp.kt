package com.freshbasket.customer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ProductDetailsResp(
        val id_customer: String?= null,
        val product: ArrayList<Product>?,
        val summary: Summary
):CommonRes() {

    @Parcelize
    data class Summary(
            val cart_count: String?= null,
            val realization: String?= null,
            val mrp: String? = null,
            val selling_price: String?= null,
            val delivery_charge: String?= null,
            val grand_total: String? = null
    ): Parcelable

    @Parcelize
    data class Product(
            val _id: String? = null,
            val name: String? = null,
            val article_no: String? = null,
            val description: String? = null,
            val tags: String? = null,
            val brand: String? = null,
            val id_category: String? = null,
            val category2_id: String? = null,
            val category1_id: String? = null,
            val category2: String? = null,
            val category1: String? = null,
            val wishlist: Int? = null,
            val pic: ArrayList<Pic>?,
            val spec: ArrayList<Spec>? = null,
            val sku: ArrayList<Sku>? = null,
            val actual_price: Int? = null,
            val mrp: Int? = null,
            val selling_price: Int? = null
    ): Parcelable {

        @Parcelize
        data class Pic(
                val _id: String? = null,
                val pic: String? = null,
                val pic_thumb: String? = null
        ): Parcelable

        @Parcelize
        data class Sku(
                val id_product: String? = null,
                val _id: String? = null,
                val sku: String? = null,
                val size: String? = null,
                val size_measuring_unit: Int? = null,
                val box_color: String? = null,
                val filter_color: String? = null,
                val tax: String? = null,
                val realization: String? = null,
                val mrp: String? = null,
                val min_quantity: String? = null,
                val stock: String? = null,
                val selling_price: Int? = null,
                val offer: String? = null,
                val mycart: String? = null
        ): Parcelable

        @Parcelize
        data class Spec(
                val _id: String? = null,
                val specification: String? = null,
                val value: String? = null
        ): Parcelable
    }
}