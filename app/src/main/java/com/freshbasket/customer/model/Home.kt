package com.freshbasket.customer.model

data class Home(
        val banner: ArrayList<Banner>?,
        val category: ArrayList<Category>?,
        val deals: ArrayList<Deal>?,
        val customercare_email: String?,
        val customercare_mobile: String?,
        val share_message: String?,
        val refferal_code: String?

)