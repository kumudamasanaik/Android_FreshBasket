package com.freshbasket.customer.screens.coupon

import com.freshbasket.customer.model.ApplyCouponRes
import com.freshbasket.customer.model.CouponRes
import com.freshbasket.customer.model.inputmodel.PromoCodeInput
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface CouponContract {
    interface View : BaseView {
        fun callCouponList()
       fun setCouponListResp(res:CouponRes)
        fun setPromoCodeResp(promoRes: ApplyCouponRes)

    }

    interface Presenter : BasePresenter<View> {
       fun callCouponListApi()
        fun callPromoCodeApi(promoCodeInput: PromoCodeInput)

    }
}