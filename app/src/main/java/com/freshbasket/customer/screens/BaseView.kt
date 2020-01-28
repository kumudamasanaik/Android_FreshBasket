package com.freshbasket.customer.screens

interface BaseView {

    fun init()

    fun showMsg(msg: String?)

    fun showLoader()

    fun hideLoader()

    fun showViewState(state: Int)
}