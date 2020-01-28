package com.freshbasket.customer.api

import retrofit2.Call
import retrofit2.Response

interface IResponseInterface {

    fun<T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String)

    fun<T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String)
}