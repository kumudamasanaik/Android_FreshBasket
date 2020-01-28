package com.freshbasket.customer.screens.landing

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.model.inputmodel.SocialSignInputModel
import retrofit2.Call
import retrofit2.Response

class LandingPresenter(view: LandingContract.View) : LandingContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: LandingContract.View? = view

    override fun doSocialsignUp(inputModel: SocialSignInputModel) {
        iResponseInterface.callApi(MyApplication.service.doSocialLogin(inputModel), ApiType.SOCIAL_LOGIN)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.SOCIAL_LOGIN ->
                    view?.socailLoginRes(response.body() as CustomerRes)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.SOCIAL_LOGIN ->
                view?.showMsg(responseError.message)

        }

    }

}