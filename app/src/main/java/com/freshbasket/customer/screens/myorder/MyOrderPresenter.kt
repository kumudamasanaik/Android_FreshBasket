package com.freshbasket.customer.screens.myorder

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.MyOrderResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class MyOrderPresenter(view: MyOrderContract.View) : MyOrderContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: MyOrderContract.View? = view

    override fun callMyOrderApi() {
            iResponseInterface.callApi(MyApplication.service.getMyOrdersList(ApiRequestParam.getMyOrderParameters()), ApiType.MY_ORDER)
    }

    override fun callCancelApi(id: String, order_no: String) {
        iResponseInterface.callApi(MyApplication.service.getCancelOrder(ApiRequestParam.geCancelOrderParameters(id, order_no)), ApiType.CANCEL_ORDER)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.MY_ORDER ->
                    view!!.setMyOrderResp(response.body() as MyOrderResp)

                ApiType.CANCEL_ORDER ->
                    view!!.setCancelApiRes(response.body() as CommonRes)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.MY_ORDER -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }

            ApiType.CANCEL_ORDER -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }
    }
}