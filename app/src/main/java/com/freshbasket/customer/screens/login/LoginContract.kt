package com.freshbasket.customer.screens.login

import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface LoginContract {
    interface View : BaseView {
        fun doLogin()
        fun invalidEmailPhone()
        fun invalidPass()
        fun setForgotPsswrdResp(res: CustomerRes)
        fun setLoginResp(res: CustomerRes)
    }

    interface Presenter : BasePresenter<View> {
        fun validateLogin(name: String?, pass: String?): Boolean
        fun validateForgotPassword(name:String?):Boolean
        fun doLogin(email: String, password: String)
        fun forgotPassowrd(name: String)

    }
}