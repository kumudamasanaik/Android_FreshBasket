package com.freshbasket.customer.screens.orderdetails

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.Order
import com.freshbasket.customer.util.Validation
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Response

class OrderDetailsPresenter(view: OrderDetailsContract.View) : OrderDetailsContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: OrderDetailsContract.View? = view

    override fun callMyOrderDetailsApi(order_no: String) {
        iResponseInterface.callApi(MyApplication.service.getOrderDetailsList(ApiRequestParam.getMyOrderDetailsParameters(order_no)), ApiType.ORDER_DETAILS)
    }

    override fun callReturnApi(id: String, order_id: String, return_reason: String, jsonArray: JsonArray) {
        iResponseInterface.callApi(MyApplication.service.getReturnOrder(ApiRequestParam.geReturnOrderParameters(id, order_id, return_reason, jsonArray)), ApiType.RETURN_ORDER)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()

        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.ORDER_DETAILS ->
                    view!!.setOrderDetailsResp(response.body() as Order)

                ApiType.RETURN_ORDER ->
                    view!!.setReturnApiRes(response.body() as CommonRes)

            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.ORDER_DETAILS -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }

            ApiType.RETURN_ORDER -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }

    }
}