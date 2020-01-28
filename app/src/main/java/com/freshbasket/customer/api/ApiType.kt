package com.freshbasket.customer.api

class ApiType {

    companion object {
        const val LOGIN: String = "login"
        const val REGISTER: String = "register"
        const val FORGOTPASSWORD: String = "forgot password"
        const val VERIFY_OTP = "verify_otp"
        const val RESEND_OTP = "resend_otp"
        const val CHANGE_PASSWORD: String = "change_password"
        const val SOCIAL_LOGIN: String = "social_login"
        const val NOTIFICATIONLIST: String = "notification"
        const val FAQ: String = "faq"
        const val HOME: String = "Home"
        const val SEARCH_PRODUCTS = "search"

        const val REFER_AND_EARN = "refer_earn"
        const val ADD_ADDRESS ="add_address"
        const val SHOP_BY_CATEGORY: String = "shop by category"
        const val SELECT_ADDRESS: String = "select address"
        const val MY_ORDER ="my checkoutOrders"
        const val CANCEL_ORDER ="cancel order "
        const val RETURN_ORDER ="return order "

        const val TRACK_ORDER ="track checkoutOrders"
        const val ORDER_DETAILS ="order_details"
        const val OFFERS ="order_details"
        const val SMART_BASKET ="smart basket"

        const val FILTER ="filter"
        const val MY_WALLET ="my wallet"
        const val CHECKOUT ="checkout"



        const val SUB_CATOEGORY: String = "sub category"
        const val ALL_CATEGORY: String = "sub category"

        const val CART: String = "cart"
        const val REMOVE_CART: String = "remove cart"

        const val WISHLIST: String = "wishlist"
        const val PRODUCT_LIST_HEADRES = "PRODUCT LIST"
        const val PRODUCT_DETAILS = "product deatils"
        const val MY_ACCOUNT = "My Account"
        const val PRODUCT_CHILD_CATEGORY_LIST = "PRODUCT CHILD LIST"
        const val MODIFY_CART = "MODIFY CART"
        const val MY_WISH_LIST: String = "wishlist"
        const val LOCALITY = "locality"
        const val ADDRESS_LIST = "address_list"
        const val UPDATEADDRESS: String = "updateaddress"
        const val DELETE_ADDRESS: String = "deladdress"
        const val CART_SUMMARY: String = "cart summary"
        const val CHECK_OUT: String = "Checkout"
        const val COUPON_LIST: String = "Coupon"
        const val PROMO_CODE: String = "Promo Code"

        const val COMBO_LIST = "combo_list"


        const val FIREBASE: String = "Firebase"
    }

}