package com.freshbasket.customer.screens.adddeliveryaddress

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.DeliveryAddRes
import com.freshbasket.customer.model.LocalityRes
import com.freshbasket.customer.model.inputmodel.Address
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class DeliveryAddressPresenter(view: DeliveryAddressContract.View?) : DeliveryAddressContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: DeliveryAddressContract.View? = view

    override fun validate(input: Address): Boolean {

        if (input.address_nickname.isNullOrEmpty()) {
            view!!.showDeliveryAddressValidateErrorMsg("1")
            return false
        }

        if (input.first_name.isNullOrEmpty()) {
            view!!.showDeliveryAddressValidateErrorMsg("2")
            return false
        }

        if (input.last_name.isNullOrEmpty()) {
            view!!.showDeliveryAddressValidateErrorMsg("3")
            return false
        }
        if (!Validation.isValidMobileNumber(input.phone)) {
            view!!.showDeliveryAddressValidateErrorMsg("4")
            return false
        }
        if (input.locality.isNullOrEmpty()) {
            view!!.showDeliveryAddressValidateErrorMsg("5")
            return false
        }

        if (input.house_no.isNullOrEmpty()) {
            view!!.showDeliveryAddressValidateErrorMsg("6")
            return false
        }
        if (input.residential_complex.isNullOrEmpty()) {
            view!!.showDeliveryAddressValidateErrorMsg("7")
            return false
        }

        if (input.area.isNullOrEmpty()) {
            view!!.showDeliveryAddressValidateErrorMsg("8")
            return false
        }
        if (!Validation.isValidpinCode(input.pincode)) {
            view!!.showDeliveryAddressValidateErrorMsg("9")
            return false
        }
        return true
    }

    override fun callgetLocality(op: String, city: String) {
        iResponseInterface.callApi(MyApplication.service.getLocality(ApiRequestParam.getLocality(op, city)), ApiType.LOCALITY)
    }

    override fun callAddDeliveryAddress(input: Address) {
        iResponseInterface.callApi(MyApplication.service.getAddDeliveryAddress(input), ApiType.ADD_ADDRESS)

    }

    override fun callUpdateAddressApi(input: Address) {
        iResponseInterface.callApi(MyApplication.service.getUpdateAddress(input), ApiType.UPDATEADDRESS)

    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.ADD_ADDRESS -> {
                    view?.setAddDeliveryAddressRes(response.body() as DeliveryAddRes)
                }
                ApiType.LOCALITY -> {
                    view?.setLocalityRes(response.body() as LocalityRes)
                }
                ApiType.UPDATEADDRESS -> {
                    view?.setupdateAddressApiRes(response.body() as DeliveryAddRes)
                }
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.ADD_ADDRESS -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
            ApiType.LOCALITY -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
            ApiType.UPDATEADDRESS -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }
    }
}