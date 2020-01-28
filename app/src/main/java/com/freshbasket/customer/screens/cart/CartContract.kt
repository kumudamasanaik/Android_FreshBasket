package com.freshbasket.customer.screens.cart

import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface CartContract {
    interface View : BaseView {
        fun callCartApi()
        fun setCartApiResp(cartres: FetchCartResp)
        fun modifycartApiRes(productlistRes: FetchCartResp?)
        fun setRemoveCartApiRes(productlistRes: FetchCartResp?)
        fun setWishListApiRes(wishlistRes: WishListResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun callCartApi(op:String)
        fun modifyCart(modifyCartIn: ModifyCartIp)
        fun modifyWishList(op:String,productId:String)
        fun callRemoveCartApi(modifyCartIn: ModifyCartIp)


    }
}