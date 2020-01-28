package com.freshbasket.customer.screens.trackorder

import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface TrackOrderContract {
    interface View : BaseView {
        fun callTrackOrder()
        fun setTrackOrderResp(res: HomeResp)
    }

    interface Presenter : BasePresenter<View> {
        fun doTrackOrder()
    }
}