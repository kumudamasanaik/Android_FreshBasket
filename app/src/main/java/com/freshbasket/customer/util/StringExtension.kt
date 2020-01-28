package com.freshbasket.customer.util


fun String.isPhone(): Boolean {
    /*val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)*/
    return (this.length == 10)
}

fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

fun String.equalsIgnoreCase(other: String) = this.toLowerCase().contentEquals(other.toLowerCase())