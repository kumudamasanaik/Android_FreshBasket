package com.freshbasket.customer.screens.filterproductlist

import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.ProductResp
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface ProductListFilterContract {

    interface View : BaseView {

        fun callProductListApi()
        fun setProductListApiResp(productlistRes: ProductResp)
        fun modifycartApiRes(productlistRes: FetchCartResp?)
        fun setWishListApiRes(wishlistRes: WishListResponse)


    }

    interface Presenter : BasePresenter<View> {
        fun getProductList(subCatId:String)
        fun modifyCart(modifyCartIn: ModifyCartIp)
        fun modifyWishList(op:String,productId:String)

    }

}