package com.freshbasket.customer.screens.productdetail

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.ProductDetailsResp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class ProductDetailsPresenter(var view: ProductDetailsContract.View?) : ProductDetailsContract.Presenter, IResponseInterface {

    override fun callProductDetailsApi(product_id:String) {
        iResponseInterface.callApi(MyApplication.service.getProductDetails(ApiRequestParam.getProductDeatilsParameters(product_id)), ApiType.PRODUCT_DETAILS)
    }

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.PRODUCT_DETAILS ->
                    view!!.setProductDetailsData(response.body() as ProductDetailsResp)
            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)

    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.PRODUCT_DETAILS -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }
    }
}