package com.freshbasket.customer.screens.landing

import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.model.inputmodel.SocialSignInputModel
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.freshbasket.customer.screens.login.LoginContract

interface LandingContract {
    interface View : BaseView {
        fun callSocialSignInApi()
        fun socailLoginRes(res: CustomerRes)
    }

    interface Presenter : BasePresenter<LoginContract.View> {
        fun doSocialsignUp(inputModel: SocialSignInputModel)
    }
}