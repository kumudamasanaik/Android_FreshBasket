package com.freshbasket.customer.screens.mywallet

import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface MyWalletContract {

    interface View : BaseView {

        fun callMyWalletApi()
        fun setMyWalletApiResp(homeResp: HomeResp)


    }

    interface Presenter : BasePresenter<View> {
        fun callMyWalletApi()

    }

}