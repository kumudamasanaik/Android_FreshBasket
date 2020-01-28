package com.freshbasket.customer.screens.cart

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.util.Validation
import retrofit2.Call
import retrofit2.Response

class CartPresenter(view: CartContract.View) : CartContract.Presenter, IResponseInterface {


    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: CartContract.View? = view

    override fun callCartApi(op: String) {
        iResponseInterface.callApi(MyApplication.service.getCart(ApiRequestParam.getCartParameters(op)), ApiType.CART)
    }

    override fun modifyCart(modifyCartIn: ModifyCartIp) {
        iResponseInterface.callApi(MyApplication.service.getModifyCart(modifyCartIn), ApiType.MODIFY_CART)
    }

    override fun modifyWishList(op: String, productId: String) {
        iResponseInterface.callApi(MyApplication.service.getWishList(ApiRequestParam.getWishListParameters(op, productId)), ApiType.WISHLIST)
    }

    override fun callRemoveCartApi(modifyCartIn: ModifyCartIp) {
        iResponseInterface.callApi(MyApplication.service.removeCart(modifyCartIn), ApiType.REMOVE_CART)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.CART ->
                    view!!.setCartApiResp(response.body() as FetchCartResp)
                ApiType.WISHLIST ->
                    view!!.setWishListApiRes(response.body() as WishListResponse)
                ApiType.MODIFY_CART ->
                    view!!.modifycartApiRes(response.body() as FetchCartResp)
                ApiType.REMOVE_CART ->
                    view!!.modifycartApiRes(response.body() as FetchCartResp)

            }
        } else
            view!!.showViewState(MultiStateView.VIEW_STATE_EMPTY)


    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)
        when (reqType) {
            ApiType.CART -> {
                view!!.showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
            ApiType.WISHLIST -> {
                view!!.showMsg(null)
            }
            ApiType.MODIFY_CART -> {
                view!!.showMsg(null)
            }
            ApiType.REMOVE_CART -> {
                view!!.showMsg(null)
            }


        }
    }


}