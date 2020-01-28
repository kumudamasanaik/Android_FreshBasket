package com.freshbasket.customer.screens.register

import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.model.inputmodel.SignUpInput
import com.freshbasket.customer.screens.BaseView

interface SignUpContract {
    interface View : BaseView {
        fun doRegister()
        fun setRegsiterRes(res: CustomerRes)
        fun showSignupValidateErrorMsg(msg: String)
    }

    interface Presenter {
        fun validate(input: SignUpInput): Boolean
        fun callRegister(input: SignUpInput)
    }
}