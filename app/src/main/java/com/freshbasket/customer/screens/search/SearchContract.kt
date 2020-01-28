package com.freshbasket.customer.screens.search

import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.SearchProductRes
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.model.inputmodel.SearchInput
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface SearchContract {
    interface View : BaseView {
        fun searchProducts()
        fun setSearchProductsRes(res: SearchProductRes)
        fun setCartApiResp(cartres: FetchCartResp)
        fun modifycartApiRes(productlistRes: FetchCartResp?)
        fun setWishListApiRes(wishlistRes: WishListResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun callSearch(input: SearchInput)
        fun callCartApi(op:String)
        fun modifyCart(modifyCartIn: ModifyCartIp)
        fun modifyWishList(op:String,productId:String)
    }
}