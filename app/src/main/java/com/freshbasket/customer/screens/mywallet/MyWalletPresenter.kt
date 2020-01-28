package com.freshbasket.customer.screens.mywallet

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class MyWalletPresenter(view: MyWalletContract.View?) : MyWalletContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: MyWalletContract.View? = view

    override fun callMyWalletApi() {
        iResponseInterface.callApi(MyApplication.service.getMyWallet(ApiRequestParam.getMyWalletParameters()), ApiType.MY_WALLET)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.MY_WALLET ->
                    view!!.setMyWalletApiResp(response.body() as HomeResp)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.MY_WALLET -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }

    }
}