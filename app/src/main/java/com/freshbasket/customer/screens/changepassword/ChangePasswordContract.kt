package com.freshbasket.customer.screens.changepassword

import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface ChangePasswordContract {
    interface View : BaseView {
        fun doChangePassword()
        fun setChangepasswordRes(res: CustomerRes)
    }

    interface Presenter : BasePresenter<View> {
        fun doChangePassword(changed_password: String)

    }
}