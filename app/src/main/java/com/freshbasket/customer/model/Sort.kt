package com.freshbasket.customer.model

data class Sort(
        val _id: String,
        val name: String,
        val pic: String,
        val type: String,
        val value: String
) {
    internal var isChecked: Boolean = false

    fun setChecked(checked: Boolean) {
        isChecked = checked
    }
}