package com.freshbasket.customer.screens.coupon

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.ApplyCouponRes
import com.freshbasket.customer.model.CouponRes
import com.freshbasket.customer.model.inputmodel.PromoCodeInput
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class CouponPresenter(view: CouponContract.View) : CouponContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: CouponContract.View? = view

    override fun callCouponListApi() {
        iResponseInterface.callApi(MyApplication.service.getCouponList(ApiRequestParam.getCouponListReqParameter()), ApiType.COUPON_LIST)

    }

    override fun callPromoCodeApi(promoCodeInput: PromoCodeInput) {
        iResponseInterface.callApi(MyApplication.service.applyPromoCode(promoCodeInput), ApiType.PROMO_CODE)

    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.COUPON_LIST ->
                    view!!.setCouponListResp(response.body() as CouponRes)
                ApiType.PROMO_CODE ->
                    view!!.setPromoCodeResp(response.body() as ApplyCouponRes)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)


    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.COUPON_LIST ->
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)

            ApiType.PROMO_CODE ->
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)


        }
    }
}

