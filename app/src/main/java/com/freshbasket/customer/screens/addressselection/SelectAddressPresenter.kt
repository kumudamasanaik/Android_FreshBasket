package com.freshbasket.customer.screens.addressselection

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.DeliveryAddRes
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class SelectAddressPresenter(view: SelectAddressContract.View?) : SelectAddressContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: SelectAddressContract.View? = view

    override fun callAddressListApi(op: String, customer_id: String) {
        iResponseInterface.callApi(MyApplication.service.getAddressList(ApiRequestParam.getAddressList(op, customer_id)),ApiType.ADDRESS_LIST)
    }

    override fun selectAddressApi(customer_id: String, op: String, address_id: String,selected:String) {
        iResponseInterface.callApi(MyApplication.service.getSelectAddressApi(ApiRequestParam.getSelectAddressParameters(customer_id,op,address_id,selected)), ApiType.SELECT_ADDRESS)
    }

    override fun deleteAddressApi(op: String, address_id: String, customer_id: String) {
        iResponseInterface.callApi(MyApplication.service.getDeleteAddress(ApiRequestParam.deleteAddress(op,address_id,customer_id)),ApiType.DELETE_ADDRESS)

    }
        override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.ADDRESS_LIST ->
                    view!!.setAddressListApiRes(response.body() as DeliveryAddRes)

                ApiType.SELECT_ADDRESS ->
                    view!!.setSelectAddressApiResp(response.body() as DeliveryAddRes)

                ApiType.DELETE_ADDRESS ->
                    view!!.setdelAddressApiRes(response.body() as DeliveryAddRes)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.ADDRESS_LIST -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }

            ApiType.SELECT_ADDRESS -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }

            ApiType.DELETE_ADDRESS -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }
    }
}