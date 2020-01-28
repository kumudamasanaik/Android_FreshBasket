package com.freshbasket.customer.screens.notification

import com.freshbasket.customer.model.NotificationResp
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface NotificationContract {
    interface View : BaseView {
        fun callNotification()
        fun setNotificationResp(res: NotificationResp)
    }

    interface Presenter : BasePresenter<View> {
        fun callNotificationApi()
    }
}