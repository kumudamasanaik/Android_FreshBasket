package com.freshbasket.customer.screens.cartsummary

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.CartSummaryResp
import com.freshbasket.customer.model.CheckOutResp
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Response

class CartSummaryPresenter(view: CartSummaryContract.View) : CartSummaryContract.Presenter, IResponseInterface {


    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: CartSummaryContract.View? = view

    override fun callCartSummaryApi() {
        iResponseInterface.callApi(MyApplication.service.getCartSummary(CommonUtils.getAutharizationKey(), ApiRequestParam.getCartSummaryReqParameter()), ApiType.CART_SUMMARY)

    }

    override fun callCheckOutApi(jsonArray: JsonArray) {
        iResponseInterface.callApi(MyApplication.service.getCheckout(ApiRequestParam.getCheckoutParameters(jsonArray)), ApiType.CHECK_OUT)

    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.CART_SUMMARY ->
                    view!!.setCartSummaryApiResp(response.body() as CartSummaryResp)
                ApiType.CHECK_OUT ->
                    view!!.setcheckoutApiResp(response.body() as CheckOutResp)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)


    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.CART_SUMMARY, ApiType.CHECK_OUT -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }

        }
    }
}