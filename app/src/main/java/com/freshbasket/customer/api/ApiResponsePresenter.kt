package com.freshbasket.customer.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiResponsePresenter constructor(var iResponseInterface: IResponseInterface) : IRequestInterface {

    override fun <T> callApi(call: Call<T>, reqType: String) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                iResponseInterface.onResponseSuccess(call, response, reqType)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                iResponseInterface.onResponseFailure(call, t, reqType)
            }
        })
    }
}