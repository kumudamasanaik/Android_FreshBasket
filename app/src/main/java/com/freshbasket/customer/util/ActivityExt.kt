package com.freshbasket.customer.util

import android.app.Application
import android.content.Context
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.freshbasket.customer.BuildConfig
import com.freshbasket.customer.R


fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}


fun AppCompatActivity.showToastMsg(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun FragmentActivity.logd(msg: String) {
    if (BuildConfig.DEBUG)
        Log.e(this.javaClass.name, msg)
}

fun FragmentActivity.loge(msg: String) {
    if (BuildConfig.DEBUG)
        Log.e(this.javaClass.name, msg)
}

fun Fragment.logd(msg: String) {
    if (BuildConfig.DEBUG)
        Log.e(this.javaClass.name, msg)
}

fun Application.logd(msg: String) {
    if (BuildConfig.DEBUG)
        Log.e(this.javaClass.name, msg)
}

fun FragmentActivity.toast(@StringRes stringRes: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringRes, duration).show()
}


fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(Context
                .INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

fun AppCompatActivity.showSnackBar(view: View, msg: String) {
    val snackbarview = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
    val snckView = snackbarview.view
    val snackViewText = snckView.findViewById(android.support.design.R.id.snackbar_text) as TextView
    snackViewText.setTextColor(this.resources.getColor(R.color.color_red))
    snckView.setBackgroundColor(this.resources.getColor(R.color.color_light_blue))
    snackbarview.show()
}


fun View.BlinkAnimation() {
    val anim = AlphaAnimation(1.0f, 0.0f)
    anim.duration = 750
    anim.fillAfter = true
    anim.repeatMode = Animation.REVERSE // ping pong mode
    anim.repeatCount = Animation.INFINITE // count of repeats
    startAnimation(anim)
}

inline fun <E : Any, T : Collection<E?>> T?.withNotNullNorEmpty(func: T.() -> Unit): Unit {
    if (this != null && this.isNotEmpty()) {
        with(this) { func() }
    }
}
