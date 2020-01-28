package com.freshbasket.customer.screens.offers

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.OfferResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class OffersPresenter(view: OffersContract.View) : OffersContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: OffersContract.View? = view


    override fun callOffersApi() {
        iResponseInterface.callApi(MyApplication.service.getOffers(), ApiType.OFFERS)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.OFFERS ->
                    view!!.setOffersResp(response.body() as OfferResp)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.OFFERS -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }

    }
}