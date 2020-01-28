package com.freshbasket.customer.screens.changepassword

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.model.CustomerRes
import retrofit2.Call
import retrofit2.Response

class ChangePasswordPresenter(view: ChangePasswordContract.View) : ChangePasswordContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: ChangePasswordContract.View? = view


    override fun doChangePassword( changed_password: String) {
        iResponseInterface.callApi(MyApplication.service.doChangePassword(ApiRequestParam.changePassword( changed_password)), ApiType.CHANGE_PASSWORD)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.CHANGE_PASSWORD ->
                    view?.setChangepasswordRes(response.body() as CustomerRes)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.CHANGE_PASSWORD ->
                view?.showMsg(responseError.message)
        }
    }
}