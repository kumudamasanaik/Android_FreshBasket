package com.freshbasket.customer.screens.myaccount

import com.freshbasket.customer.model.MyAccountResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface MyAccountActivityContract {
    interface View : BaseView {
        fun updateMyProfile()
        fun setMyAccountApiRes(res: MyAccountResp)

    }

    interface Presenter : BasePresenter<View> {
        fun updateProfile(first_name:String, email:String, pic:String)
    }
}