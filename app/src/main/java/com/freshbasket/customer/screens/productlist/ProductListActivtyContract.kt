package com.freshbasket.customer.screens.productlist

import com.freshbasket.customer.model.ProductListHeaderResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface ProductListActivtyContract {
    interface View : BaseView {
        fun getViewPagerHeaderData()
        fun setViewPagerHeaderData(productlistHeaderRes: ProductListHeaderResp)
    }

    interface Presenter : BasePresenter<View> {
        fun callChildCategoryApi(catId: String)
    }
}