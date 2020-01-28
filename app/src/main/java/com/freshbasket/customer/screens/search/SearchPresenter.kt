package com.freshbasket.customer.screens.search

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.SearchProductRes
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.model.inputmodel.SearchInput
import retrofit2.Call
import retrofit2.Response

class SearchPresenter(view: SearchContract.View) : SearchContract.Presenter, IResponseInterface {


    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: SearchContract.View? = view

    override fun callSearch(input: SearchInput) {
        iResponseInterface.callApi(MyApplication.service.getSearchResult(input), ApiType.SEARCH_PRODUCTS)

    }
    override fun callCartApi(op: String) {
        iResponseInterface.callApi(MyApplication.service.getCart(ApiRequestParam.getCartParameters(op)), ApiType.CART)
    }

    override fun modifyCart(modifyCartIn: ModifyCartIp) {
        iResponseInterface.callApi(MyApplication.service.getModifyCart(modifyCartIn), ApiType.MODIFY_CART)
    }

    override fun modifyWishList(op: String, productId: String) {
        iResponseInterface.callApi(MyApplication.service.getWishList(ApiRequestParam.getWishListParameters(op, productId)), ApiType.WISHLIST)
    }



    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.SEARCH_PRODUCTS ->
                    view?.setSearchProductsRes(response.body() as SearchProductRes)
                ApiType.CART ->
                    view!!.setCartApiResp(response.body() as FetchCartResp)
                ApiType.WISHLIST ->
                    view!!.setWishListApiRes(response.body() as WishListResponse)
                ApiType.MODIFY_CART ->
                    view!!.modifycartApiRes(response.body() as FetchCartResp)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
    }


}