package com.freshbasket.customer.util

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager

fun Context.getScreenHeight(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        wm.defaultDisplay.getRealSize(point)
    } else {
        wm.defaultDisplay.getSize(point)
    }
    return point.y
}

fun Context.getScreenWidth(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        wm.defaultDisplay.getRealSize(point)
    else
        wm.defaultDisplay.getSize(point)

    return point.x
}