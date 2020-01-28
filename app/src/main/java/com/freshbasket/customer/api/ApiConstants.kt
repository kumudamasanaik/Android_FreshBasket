package com.freshbasket.customer.api

class ApiConstants {
    companion object {
        const val TOKEN = "token"
        const val SESSION = "session_id"
        const val BASE_URL = "http://13.251.141.190:80/freshbasket/api/"
        const val IMAGE_BASE_URL = "http://13.251.141.190/freshbasket/"
        const val LOGIN: String = "customer/login"
        const  val SIGN_UP = "customer"
        const val FORGOT_PASSWORD = "customer/forgotpwd"
        const val SEARCH_PRODUCTS = "item/search"
        const val NOTIFICATION = "master/notificationlist"
        const val FAQ = "admin"
        const val VERIFY_OTP = "customer/otp"
        const val RESEND_OTP = "customer/resend_otp"
        const val CHANGE_PASSWORD = "customer/update"
        const val SOCIAL_LOGIN = "customer/social"
        const val HOME = "customer/dashboard"
        const val UPDATE_FIREBASE_TOKEN_TO_SERVER = "admin/device"
        const val REFER_AND_EARN = "customer/refer_earn"
        const val DELIVERY_ADDRESS = "customer/delivery_add"
        const val ADD_ADDRESS = "customer/address"
        const val ADDRESS_LIST="customer/address"
        const val SHOPBYCATEGORY = " "/*TO NEED TO ADD main SUBCATEGORY URL*/
        const val GET_ALL_CATEGORY: String = "category/allcategory"
        const val PRODUCT_LISTING= "item/product "/*TO NEED TO ADD CHECKOUT URL*/
        const val CART = "cart"
        const val REMOVE_CART = "cart/remove"
        const val SMART_BASKET = "combo/details"
        const val COMBO_LIST = "combo/all"

        const val MYWISHLIST: String = "wishlist"
        const val PRODUCT_LIST_HEADER = "category/subcategoryadmin"/*TO NEED TO ADD PRODUCT LIST URL*/
        const val MY_ACCOUNT = "customer/update"
        const val SELECT_DELIVERY_ADDRESS = "customer/address"
        const val MY_ORDER = "customer/orders"
        const val TRACK_ORDER = " "/*TO NEED TO ADD TRACK ORDER URL*/
        const val ORDER_DETAILS = "order/specific"
        const val OFFERS= "offers/all"
        const val OFFERS_DETAILS= "offers/details"
        const val PRODUCT_DETAILS="item/specific"

        const val FILTER= "item/filterdata "
        const val MY_WALLET= " "/*TO NEED TO ADD MY WALLET URL*/
        const val CHECKOUT= "cart/checkout"/*TO NEED TO ADD CHECKOUT URL*/
        const val MODIFY_CART= "cart"
        const val GET_LOCALITY="admin"
        const val COUPON_LIST="cart/allpromocode"
        const val APPLY_PROMO_CODE="cart/promocode"


        const val UPDATEADDRESS: String = "customer/address"
        const val DELADDRESS: String = "customer/address"
        const val CART_SUMMARY: String = "cart/summary"
        const val CANCEL = "order "/*Cancel order*/
        const val ORDER_RETURN = "orderreturn "/*Cancel order*/




    }

}