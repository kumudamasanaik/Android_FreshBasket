package com.freshbasket.customer.model.inputmodel

data class SignUpInput(
        val first_name: String?,
        val last_name: String?,
        val gender: String?,
        val email: String?,
        val mobile: String?,
        val password: String?,
        var confirm_password: String?=null,
        val pancard_pic: String?,
        val aadhar_pic: String?,
        val bank_acc_number: String?,
        val ifsc_code: String?,
        val sms_key: String?,
        val referred_code: String? = null



)
