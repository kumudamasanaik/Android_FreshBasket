package com.freshbasket.customer.screens.productdetail

import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.model.ProductDetailsResp
import com.freshbasket.customer.model.ProductListHeaderResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface ProductDetailsContract {
    interface View : BaseView {
        fun callProductDetails()
        fun setProductDetailsData(res:ProductDetailsResp)
    }

    interface Presenter : BasePresenter<View> {
        fun callProductDetailsApi(product_id:String)
    }
}