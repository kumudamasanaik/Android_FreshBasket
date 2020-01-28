package com.freshbasket.customer.screens.productlist.fragment

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.ProductResp
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class ProductListFragmentPresenter(var view: ProductListFragmentContract.View?) : ProductListFragmentContract.Presenter, IResponseInterface {


    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)


    override fun modifyCart(modifyCartIn: ModifyCartIp) {
        iResponseInterface.callApi(MyApplication.service.getModifyCart(modifyCartIn), ApiType.MODIFY_CART)
    }

    override fun callProductListApi(catId: String) {
        iResponseInterface.callApi(MyApplication.service.getALlProductList(ApiRequestParam.getChildProductListing(catId)), ApiType.PRODUCT_LIST_HEADRES)

    }

    override fun modifyWishList(op: String,productId:String) {
        iResponseInterface.callApi(MyApplication.service.getWishList(ApiRequestParam.getWishListParameters(op,productId)), ApiType.WISHLIST)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.PRODUCT_LIST_HEADRES ->
                    view!!.setProductListApiRes(response.body() as ProductResp)

                ApiType.MODIFY_CART ->
                    view!!.modifycartApiRes(response.body() as FetchCartResp)

                ApiType.WISHLIST ->
                    view!!.setWishListApiRes(response.body() as WishListResponse)
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
            ApiType.WISHLIST -> {
                view!!.showMsg(null)
            }
            ApiType.MODIFY_CART -> {
                view!!.showMsg(null)
            }
        }
    }
}