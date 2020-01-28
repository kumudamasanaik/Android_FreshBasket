package com.freshbasket.customer.screens.register

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.model.inputmodel.SignUpInput
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class SignUpPresenter(view: SignUpContract.View?) : SignUpContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: SignUpContract.View? = view

    override fun validate(input: SignUpInput): Boolean {

        if (input.first_name.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("1")
            return false
        }

        if (input.last_name.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("2")
            return false
        }

        if (input.gender.isNullOrEmpty() || input.gender!!.contentEquals(Constants.GENDER_SIGNUP)) {
            view!!.showSignupValidateErrorMsg("3")
            return false
        }

        if (!Validation.isValidMobileNumber(input.mobile)) {
                view!!.showSignupValidateErrorMsg("4")
                return false
        }

        if (!Validation.isValidEmail(input.email!!)) {
                view!!.showSignupValidateErrorMsg("5")
                return false
        }

        if(!Validation.isValidPassword(input.password!!)){
            view!!.showSignupValidateErrorMsg("6")
            return false
        }

        /*if (input.password.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("6")
            return false
        }*/

        if (!input.confirm_password.equals(input.password)) {
            view!!.showSignupValidateErrorMsg("7")
            return false
        }



//        if (input.bank_acc_number.isNullOrEmpty()) {
//            view!!.showSignupValidateErrorMsg("8")
//            return false
//        }
//        if (input.ifsc_code.isNullOrEmpty()) {
//            view!!.showSignupValidateErrorMsg("9")
//            return false
//        }
        return true
    }

    override fun callRegister(input: SignUpInput) {
        iResponseInterface.callApi(MyApplication.service.doRegister(input), ApiType.REGISTER)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.REGISTER -> {
                    view?.setRegsiterRes(response.body() as CustomerRes)
                }
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.REGISTER ->
                view?.showMsg(responseError.message)
        }
    }
}