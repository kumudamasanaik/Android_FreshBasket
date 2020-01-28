package com.freshbasket.customer.screens.wishlist

import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface WishListContract {
    interface View : BaseView {
        fun callWishListApi()
        fun setWishListApiRes(wishlistRes: WishListResponse)
        fun modifycartApiRes(productlistRes: FetchCartResp?)
        fun setWishListModifiedRes(wishlistRes: WishListResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun callWishListApi(op: String)
        fun modifyCart(modifyCartIn: ModifyCartIp)
        fun modifyWishList(op:String,productId:String)
    }
}