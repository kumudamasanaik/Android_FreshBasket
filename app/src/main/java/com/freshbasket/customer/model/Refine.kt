package com.freshbasket.customer.model

import com.freshbasket.customer.constants.Constants
import com.google.gson.annotations.SerializedName

data class Refine(
        val name: String,

        @SerializedName(value = Constants.BRAND, alternate = [Constants.PRICE])
        val refineValues: ArrayList<Sort>
) {
    private var isSelected: Boolean = false


    fun isSelected(): Boolean {
        return isSelected
    }

    fun setSelected(selected: Boolean) {
        this.isSelected = selected
    }
}