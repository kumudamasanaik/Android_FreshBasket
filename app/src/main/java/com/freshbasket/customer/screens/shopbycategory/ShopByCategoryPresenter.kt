package com.freshbasket.customer.screens.shopbycategory

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.SubCategoryResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class ShopByCategoryPresenter(view: ShopByCategoryContract.View) : ShopByCategoryContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: ShopByCategoryContract.View? = view

    override fun callSubCategoryApi() {
        iResponseInterface.callApi(MyApplication.service.getAllCategoryList(), ApiType.ALL_CATEGORY)
    }


    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.ALL_CATEGORY ->
                    view!!.setSubCategoryApiRes(response.body() as SubCategoryResp)

            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.ALL_CATEGORY -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }

        }
    }

}