package com.freshbasket.customer.screens.offersdetails

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.OrderDetailsResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class OfferDetailPresenter(view: OfferDetailContract.View) : OfferDetailContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: OfferDetailContract.View? = view


    override fun callOfferDetailsApi(_id: String) {
        iResponseInterface.callApi(MyApplication.service.getOffersDetails(ApiRequestParam.getOffersParameters(_id)), ApiType.ORDER_DETAILS)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.ORDER_DETAILS ->
                    view!!.setOfferDetailsResp(response.body() as OrderDetailsResp)
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
        }

    }
}