package com.freshbasket.customer.screens.offersdetails

import com.freshbasket.customer.model.OrderDetailsResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface OfferDetailContract {
    interface View : BaseView {
        fun callOfferDeatils()
        fun setOfferDetailsResp(res: OrderDetailsResp)
    }

    interface Presenter : BasePresenter<View> {
        fun callOfferDetailsApi(_id:String)
    }
}