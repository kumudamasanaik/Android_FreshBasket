package com.freshbasket.customer.screens.smartbasket

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.ComboDetailResp
import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class SmartBasketPresenter(view: SmartBasketContract.View) : SmartBasketContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: SmartBasketContract.View? = view


    override fun callSmartBasketApi(cat_id:String) {
        iResponseInterface.callApi(MyApplication.service.getSmartBasketData(ApiRequestParam.getComboDetailsParameters(cat_id)), ApiType.SMART_BASKET)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.SMART_BASKET ->
                   view!!.setSmartBasketResp(response.body() as ComboDetailResp)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.SMART_BASKET -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }

    }
}