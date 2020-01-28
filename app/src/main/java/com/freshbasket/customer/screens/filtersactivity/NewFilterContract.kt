package com.freshbasket.customer.screens.filtersactivity

import com.freshbasket.customer.model.NewFilterResUpdated
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface NewFilterContract {

    interface View : BaseView {

        fun callFilterApi()
        fun setFilterApiResp(res: NewFilterResUpdated)


    }

    interface Presenter : BasePresenter<View> {
        fun callFilterApi(category_id: String)

    }

}