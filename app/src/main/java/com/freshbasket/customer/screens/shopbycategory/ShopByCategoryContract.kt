package com.freshbasket.customer.screens.shopbycategory

import com.freshbasket.customer.model.SubCategoryResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface ShopByCategoryContract {
    interface View : BaseView {
        fun callShopByCategoryApi()
        fun setSubCategoryApiRes(subCategoryResp: SubCategoryResp)
 }

    interface Presenter : BasePresenter<View> {
        fun callSubCategoryApi()

    }
}