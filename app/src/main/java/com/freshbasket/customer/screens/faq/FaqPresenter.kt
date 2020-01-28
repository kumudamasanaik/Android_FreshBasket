package com.freshbasket.customer.screens.faq

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.FaqRes
import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class FaqPresenter(view: FaqContract.View) : FaqContract.Presenter, IResponseInterface {


    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: FaqContract.View? = view


    override fun callFAQApi(op: String, table: String) {
        iResponseInterface.callApi(MyApplication.service.getFaqData(ApiRequestParam.getFaqList(op,table)),ApiType.FAQ)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.FAQ->
                    view!!.setFaqResp(response.body() as FaqRes)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.FAQ -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }

    }
}