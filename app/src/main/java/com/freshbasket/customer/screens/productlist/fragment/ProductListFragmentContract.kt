package com.freshbasket.customer.screens.productlist.fragment

import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.model.ProductResp
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface ProductListFragmentContract {
    interface View : BaseView {
        fun callProductListApi()
        fun setProductListApiRes(productlistRes: ProductResp)
        fun modifycartApiRes(productlistRes: FetchCartResp?)
        fun setWishListApiRes(wishlistRes: WishListResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun callProductListApi(catId:String)
        fun modifyCart(modifyCartIn: ModifyCartIp)
        fun modifyWishList(op:String,productId:String)
    }
}