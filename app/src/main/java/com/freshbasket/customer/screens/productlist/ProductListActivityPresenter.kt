package com.freshbasket.customer.screens.productlist

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.ProductListHeaderResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class ProductListActivityPresenter(var view: ProductListActivtyContract.View?) : ProductListActivtyContract.Presenter, IResponseInterface {

    override fun callChildCategoryApi(catId: String) {
        iResponseInterface.callApi(MyApplication.service.getChildCategoryForViewPageHeaders(ApiRequestParam.getProductListParameters(catId)), ApiType.PRODUCT_LIST_HEADRES)
    }

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.PRODUCT_LIST_HEADRES ->
                    view!!.setViewPagerHeaderData(response.body() as ProductListHeaderResp)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.PRODUCT_LIST_HEADRES -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }
    }
}