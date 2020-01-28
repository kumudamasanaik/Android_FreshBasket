package com.freshbasket.customer.model

import com.freshbasket.customer.constants.Constants

open class CommonRes {
    var status: String? = null
    var message: String? = null
    var key: String? = null
    var code: String? = null
    var _session: String? = null
    var error_code:String?= null



    fun isSuccess(): Boolean {
        return status!!.contentEquals(Constants.SUCCESS)
    }
}






