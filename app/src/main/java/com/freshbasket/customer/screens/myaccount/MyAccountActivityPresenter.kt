package com.freshbasket.customer.screens.myaccount

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.MyAccountResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class MyAccountActivityPresenter(view: MyAccountActivityContract.View) : MyAccountActivityContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: MyAccountActivityContract.View? = view

    override fun updateProfile(first_name: String, email: String, pic: String) {
        iResponseInterface.callApi(MyApplication.service.getMyAccountList(ApiRequestParam.getMyAccountParameters(first_name, email, pic)), ApiType.MY_ACCOUNT)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.MY_ACCOUNT ->
                    view!!.setMyAccountApiRes(response.body() as MyAccountResp)
            }
        } else {
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
       // view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.MY_ACCOUNT -> {
                view!!.showMsg(null)
            }
        }
    }
}