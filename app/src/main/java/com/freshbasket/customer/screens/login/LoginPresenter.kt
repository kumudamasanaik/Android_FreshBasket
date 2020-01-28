package com.freshbasket.customer.screens.login

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.model.CustomerRes
import retrofit2.Call
import retrofit2.Response

class LoginPresenter(view: LoginContract.View) : LoginContract.Presenter, IResponseInterface {


    override fun validateForgotPassword(name: String?): Boolean {
        if (name.isNullOrEmpty()) {
            view?.invalidEmailPhone()
            return false
        }
        return true
    }

    override fun validateLogin(name: String?, pass: String?): Boolean {
        if (name.isNullOrEmpty()) {
            view?.invalidEmailPhone()
            return false
        }
        if (pass.isNullOrEmpty()) {
            view?.invalidPass()
            return false
        }
        return true
    }


    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: LoginContract.View? = view


    override fun doLogin(email: String, password: String) {
        iResponseInterface.callApi(MyApplication.service.doLogin(ApiRequestParam.login(email, password)), ApiType.LOGIN)
    }

    override fun forgotPassowrd(name: String) {
        iResponseInterface.callApi(MyApplication.service.doForgotPassword(ApiRequestParam.getforgotPasswrd(name)), ApiType.FORGOTPASSWORD)
    }

//    override fun doSocialsignUp(inputModel: SocialSignInputModel) {
//        iResponseInterface.callApi(MyApplication.service.doSocialLogin(inputModel), ApiType.SOCIAL_LOGIN)
//    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.LOGIN ->
                    view?.setLoginResp(response.body() as CustomerRes)
                ApiType.FORGOTPASSWORD ->
                    view?.setForgotPsswrdResp(response.body() as CustomerRes)
                //ApiType.SOCIAL_LOGIN->
                    //view?.socailLoginRes(response.body() as CustomerRes)
            }
        }


    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.LOGIN ->
                view?.showMsg(responseError.message)
            ApiType.FORGOTPASSWORD ->
                view?.showMsg(responseError.message)

        }

    }
}