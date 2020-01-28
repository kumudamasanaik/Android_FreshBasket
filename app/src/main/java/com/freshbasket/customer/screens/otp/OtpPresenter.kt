package com.freshbasket.customer.screens.otp

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.CustomerRes
import retrofit2.Call
import retrofit2.Response

class OtpPresenter(view: OtpContract.View) : OtpContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: OtpContract.View? = view

    override fun verifyOtp(otp: String, email_id: String) {

        iResponseInterface.callApi(MyApplication.service.verifyOtp(ApiRequestParam.verifyOtp(otp,email_id)),ApiType.VERIFY_OTP)
    }

    override fun resendOtp(email_id: String) {
        iResponseInterface.callApi(MyApplication.service.resendOtp(ApiRequestParam.getforgotPasswrd(email_id)), ApiType.RESEND_OTP)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.VERIFY_OTP ->
                    view?.setVerifyOtpRes(response.body() as CustomerRes)
                ApiType.RESEND_OTP->
                    view?.setResendOtpRes(response.body() as CommonRes)

            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()

        when (reqType) {
            ApiType.VERIFY_OTP ->
                view?.showMsg(responseError.message)
            ApiType.RESEND_OTP->
                view?.showMsg(responseError.message)

        }
    }
}