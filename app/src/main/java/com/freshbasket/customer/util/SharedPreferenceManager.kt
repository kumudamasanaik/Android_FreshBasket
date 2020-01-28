package com.freshbasket.customer.util

import android.content.Context
import android.content.SharedPreferences
import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.model.CartSummary
import com.freshbasket.customer.model.Category
import com.freshbasket.customer.model.Home
import com.freshbasket.customer.model.HomeResp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferenceManager {

    enum class VALUE_TYPE {
        BOOLEAN, INTEGER, STRING, FLOAT, LONG
    }

    companion object {
        val PREFERENCE_NAME = "app_pref"
        val IS_LOGED_IN = "IS_LOGED_IN"
        val SCREEN_WIDTH = "SCREEN_WIDTH"
        val SCREEN_HEIGHT = "SCREEN_HEIGHT"
        val USER_DATA = "USER_DATA"
        val USER_ID = "USER_ID"
        val PHONE = "PHONE"
        val TOKEN = "TOKEN"
        val BOARD_ID = "BOARD_ID"
        val CLASS_ID = "CLASS_ID"
        val CLASS_NAME = "CLASS_NAME"
        val BOARD_NAME = "BOARD_NAME"
        val NAME = "NAME"
        val SUBSCRIPTION_ID = "SUBSCRIPTION_ID"
        val SUBSCRIPTION_NAME = "SUBSCRIPTION_NAME"
        val SUBSCRIPTION_VALIDITY = "SUBSCRIPTION_VALIDITY"
        val SUBSCRIPTION_PRICE = "SUBSCRIPTION_PRICE"
        val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        val MAIN_CAT_LIST_DATA = "MAIN_CAT_LIST_DATA"
        var PREF_CART_DATA = "pref_cart_data"
         val CUSTOMER_EMAIL = "CUSTOMER_EMAIL"
         val CUSTOMER_MOBILE_NUMBER = "CUSTOMER_MOBILE_NUMBER"
        val SHARE_MSG = "SHARE_MSG"
        val REFERRAL_CODE = "REFERRAL_CODE"

        val PREF_All_CAT = "pref_all_cat"
        val PREF_LOC = "pref_loc"
        val OTP_VERIFICATION_KEY = "otp key"

        val MY_ADDRESS = "my address"

        fun clearPreferences() {
            getPrefs().edit().clear().apply()
        }

        fun getPrefs(): SharedPreferences {
            return MyApplication.context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        fun setPrefVal(key: String, value: Any, vType: VALUE_TYPE) {
            when (vType) {
                VALUE_TYPE.BOOLEAN -> getPrefs().edit().putBoolean(key, value as Boolean).apply()
                VALUE_TYPE.INTEGER -> getPrefs().edit().putInt(key, value as Int).apply()
                VALUE_TYPE.STRING -> getPrefs().edit().putString(key, value as String).apply()
                VALUE_TYPE.FLOAT -> getPrefs().edit().putFloat(key, value as Float).apply()
                VALUE_TYPE.LONG -> getPrefs().edit().putLong(key, value as Long).apply()
                else -> {
                }
            }
        }

        fun getPrefVal(key: String, defValue: Any, vType: VALUE_TYPE): Any? {
            val `object`: Any?
            when (vType) {
                VALUE_TYPE.BOOLEAN -> `object` = getPrefs().getBoolean(key, defValue as Boolean)
                VALUE_TYPE.INTEGER -> `object` = getPrefs().getInt(key, defValue as Int)
                VALUE_TYPE.STRING -> `object` = getPrefs().getString(key, defValue as String)
                VALUE_TYPE.FLOAT -> `object` = getPrefs().getFloat(key, defValue as Float)
                VALUE_TYPE.LONG -> `object` = getPrefs().getLong(key, defValue as Long)
            }
            return `object`
        }

        fun isLogIn(): Boolean {
            return getPrefVal(IS_LOGED_IN, false, VALUE_TYPE.BOOLEAN) as Boolean
        }

        fun saveCartData(cartSummery: CartSummary?) = setPrefVal(PREF_CART_DATA, Gson().toJson(cartSummery), VALUE_TYPE.STRING)

        fun getCartData(): CartSummary? {
            val gson = getPrefVal(PREF_CART_DATA, "", VALUE_TYPE.STRING) as String
            try {
                return Gson().fromJson<CartSummary>(gson, CartSummary::class.java) as CartSummary
            } catch (exp: Exception) {
            }
            return null
        }

        fun saveCategoryListData(categoryList: ArrayList<Category>?) = setPrefVal(MAIN_CAT_LIST_DATA, Gson().toJson(categoryList), VALUE_TYPE.STRING)

        fun getCategoryListData(): ArrayList<Category>? {
            val json = getPrefVal(MAIN_CAT_LIST_DATA, "", VALUE_TYPE.STRING) as String
            if (Validation.isValidString(json)) {
                val type = object : TypeToken<List<Category>>() {}.type
                return Gson().fromJson<ArrayList<Category>>(json, type)
            }
            return null
        }
    }
}