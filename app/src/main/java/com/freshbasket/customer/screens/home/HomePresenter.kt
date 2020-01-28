package com.freshbasket.customer.screens.home

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class HomePresenter(view: HomeContract.View?) : HomeContract.Presenter, IResponseInterface {


    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: HomeContract.View? = view

    override fun callDashBoardApi() {
        iResponseInterface.callApi(MyApplication.service.getHomeApi(ApiRequestParam.getHomeParameters()), ApiType.HOME)
    }

    override fun callFireBaseTokenToServer() {
        iResponseInterface.callApi(MyApplication.service.updateFirebaseTokeToServer(ApiRequestParam.getFireBaseToken()), ApiType.FIREBASE)

    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.HOME ->
                    view!!.setHomeApiResp(response.body() as HomeResp)

                ApiType.FIREBASE ->
                    view!!.setFireBaseTokenToApiResp(response.body() as CommonRes)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.HOME -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }

            ApiType.FIREBASE ->
                view!!.showMsg(responseError.message)
        }

    }
}