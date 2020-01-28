package com.freshbasket.customer.screens.offers

import com.freshbasket.customer.model.OfferResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface OffersContract {
    interface View : BaseView {
        fun callOffers()
        fun setOffersResp(res: OfferResp)
    }

    interface Presenter : BasePresenter<View> {
        fun callOffersApi()
    }
}