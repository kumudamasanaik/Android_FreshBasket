package com.freshbasket.customer.screens.home

import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface HomeContract {

    interface View : BaseView {

        fun callHomeBoardApi()
        fun setHomeApiResp(homeResp: HomeResp)
        fun callFireBaseTokenToServer()
        fun setFireBaseTokenToApiResp(responce:CommonRes)

    }

    interface Presenter : BasePresenter<View> {
        fun callDashBoardApi()
        fun callFireBaseTokenToServer()

    }

}