package com.freshbasket.customer.screens.combo

import com.freshbasket.customer.model.ComboOfferRes
import com.freshbasket.customer.model.DeliveryAddRes
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface ComboContract {

    interface View : BaseView {

        fun callComboListApi()
        fun setComboListApiRes(res: ComboOfferRes)
        fun modifycartApiRes(productlistRes: FetchCartResp?)
        fun setWishListApiRes(wishlistRes: WishListResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun callComboListApi()
        fun modifyCart(modifyCartIn: ModifyCartIp)
        fun modifyWishList(op:String,productId:String)
    }

}