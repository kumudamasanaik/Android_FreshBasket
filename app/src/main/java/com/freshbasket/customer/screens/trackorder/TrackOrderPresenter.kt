package com.freshbasket.customer.screens.trackorder

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class TrackOrderPresenter(view: TrackOrderContract.View) : TrackOrderContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: TrackOrderContract.View? = view


    override fun doTrackOrder() {
        iResponseInterface.callApi(MyApplication.service.doTrackOrder(),ApiType.TRACK_ORDER)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.TRACK_ORDER ->
                    view!!.setTrackOrderResp(response.body() as HomeResp)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.TRACK_ORDER -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }

    }
}