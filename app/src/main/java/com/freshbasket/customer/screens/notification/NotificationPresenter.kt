package com.freshbasket.customer.screens.notification

import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.api.ApiRequestParam
import com.freshbasket.customer.api.ApiResponsePresenter
import com.freshbasket.customer.api.ApiType
import com.freshbasket.customer.api.IResponseInterface
import com.freshbasket.customer.model.NotificationResp
import retrofit2.Call
import retrofit2.Response

class NotificationPresenter(view: NotificationContract.View) : NotificationContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: NotificationContract.View? = view


    override fun callNotificationApi() {
        iResponseInterface.callApi(MyApplication.service.getNotification(ApiRequestParam.getNotificationData()), ApiType.NOTIFICATIONLIST)

    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.NOTIFICATIONLIST ->
                    view?.setNotificationResp(response.body() as NotificationResp)
            }
        }


    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        view?.showMsg(responseError.message)

    }
}