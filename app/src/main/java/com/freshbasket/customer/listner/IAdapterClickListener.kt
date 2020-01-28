package com.freshbasket.customer.listner

interface IAdapterClickListener {
    fun onclick(item: Any, pos: Int = 0, type: String? = "none", op: String = "none")
}