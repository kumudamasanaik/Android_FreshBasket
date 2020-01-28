package com.freshbasket.customer.screens.filtersactivity

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.NewFilterResUpdated
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class NewFilterPresenter(view: NewFilterContract.View?) : NewFilterContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: NewFilterContract.View? = view

    override fun callFilterApi(category_id: String) {
        iResponseInterface.callApi(MyApplication.service.getFilter(ApiRequestParam.getFilterParameters(category_id)), ApiType.FILTER)

    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.FILTER ->
                    view!!.setFilterApiResp(response.body() as NewFilterResUpdated)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.FILTER -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }
    }
}