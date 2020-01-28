package com.freshbasket.customer.api

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

object NetworkStatus {

    private var instance: NetworkStatus? = null

    fun isOnline2(context: Context?): Boolean {
        var context = context
        var connected = false
        try {
            val connectivityManager = context!!.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            connected = networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
            return connected
        } catch (e: Exception) {
            Log.v("connectivity", e.toString())
        }

        return connected
    }
}