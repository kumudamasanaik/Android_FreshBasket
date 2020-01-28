package com.freshbasket.customer.constants

class Constants {
    companion object {
        var SOURCE = "source"
        var SOURCE_REGISTRATION = "source_registration"
        var SOURCE_LOGIN = "source_login"
        var SOURCE_TYPE = "source_type"

        /*api*/
        const val status = "status"
        const val message = "message"
        const val SUCCESS = "success"
        const val CANCELLED = "Cancelled"
        const val PENDING = "Pending"
        const val PLACED = "Placed"



        //login customerdata constants
        const val IS_USER_LOGIN = "isUserLogin"
        const val CUSTOMER_ID = "_id"
        const val AADHAR_PIC = "aadhar_pic"
        const val APP_VERSION = "app_version"
        const val BANK_ACCOUNT_NUM = "bank_acc_number"
        const val BIRTHDAY = "birthday"
        const val CUSTOMER_TYPE = "customer_type"
        const val EMAIL = "email"
        const val EMAIL_VERIFIED = "email_verified"
        const val FACEBOOK_ID = "facebook_id"
        const val FIRST_NAME = "first_name"
        const val GENDER = "gender"
        const val GOOGLE_ID = "google_id"
        const val IFSC_CODE = "ifsc_code"
        const val LAST_NAME = "last_name"
        const val LINKEDIN_ID = "linkedin_id"
        const val MOBILE = "mobile"
        const val NEWS_LETTER = "newsletter"
        const val NOTE = "note"
        const val OTP = "otp"
        const val PANCARD_PIC = "pancard_pic"
        const val PIC = "pic"
        const val REFERRAL_APPLIED = "referral_applied"
        const val REFERRAL_CODE = "referral_code"
        const val REFERRAL_USED = "referral_used"
        const val REFERRAL_AMOUNT = "referred_amount"
        const val REFERRED_BY = "referred_by"
        const val REFERRED_CODE = "referred_code"
        const val VERIFIED = "verified"
        const val WALLET = "wallet"
        const val WEBSITE = "website"
        const val PASSWORD = "password"

        const val CATEGORYLIST = "category list"
        const val PIC_DATA = "picdata"
        // Login
        const val OP = "op"
        const val AUTHORIZATION_KEY = "AUTHORIZATION_KEY"
        const val APPLY_PROMO_CODE = "apply promo code"
        const val PROMO_CODE_AMT = "apply promo code amount"

        const val AUTHORIZATION = "Authorization"
        const val KEY = "key"

        const val email = "email"
        const val password = "password"
        const val DEVICE_TYPE = "device_type"
        const val ANDROID = "android"
        const val SMS_KEY = "sms_key"
        const val APP_TYPE_CUSTOMER = "customer"
        const val APP_TYPE_STORE_MANAGER = "storemanager"
        const val APP_TYPE_DELIVERY_BOY = "deliveryboy"
        //register
        var SOCIAL_TYPE_FACEBOOK = "SOCIAL_TYPE_FACEBOOK"
        var SOCIAL_TYPE_GOOGLE = "SOCIAL_TYPE_GOOGLE"

        const val PHONE = "phone"
        const val SOCIALTYPE = "SOCIALTYPE"

        /*GET CART*/
        const val CART_GET = "get"


        /*OP TYPES*/
        const val OP_SELECT_ADDRESS = "Select Address"
        const val OP_SHOP_BY_CATEGORY = "Shop By cate"
        const val EXIT_CONFIRMATION = "alert confirmation"

        /*ADAPTER TYPES*/
        const val TYPE_DEALS_ADAPTER = "deals Categoruy"
        const val TYPE_MAIN_CAT_ADAPTER = "Main Categoruy"
        const val TYPE_SEARCH_ADAPTER = "Search Adapter"

        const val TYPE_CART_ADAPTER = "Cart Adapter"

        const val TYPE_REMOVE_CART = " Remove Cart "

        const val TYPE_SHOP_BY_CATEGORY_ADAPTER = "Shop By Category Adapter"
        const val TYPE_NOTIFICATION_ADAPTER = "Notification Adapter"
        const val TYPE_MY_ORDER_ADAPTER = "MyOrder Adapter"
        const val TYPE_CHECK_OUT_ADAPTER = "CheckOut Adapter"
        const val TYPE_ORDER_DETAIL_ADAPTER = "OrderDetail Adapter"
        const val TYPE_FAQ_ADAPTER = "Faq Adapter"
        const val TYPE_CART_SUMMARY_ADAPTER = "Cart Summary Adapter"

        const val TYPE_SELECT_ADDRESS_ADAPTER = "SelectAddress Adapter"
        const val TYPE_ADDRESS_LIST_ADAPTER = "Address list Adapter"
        const val TYPE_WISH_LIST = "Wish List"
        const val TYPE_MY_ORDER_LIST = "MyOrder List"

        const val TYPE_PRODUCT_LIST = "Product List"

        const val HOME_CART_LIST = "Cart Home List"
        const val BUTTON_SELECTED = "Button selected"




        // const val TYPE_MAIN_CAT_ADAPTER = "Main Category"
        const val TYPE_TRACK_ORDER_ADAPTER = "Track CheckoutOrders"
        const val TYPE_PRODUCT_LIST_ADAPTER = "Product List"
        const val TYPE_PRODUCT_DETAILS_ADAPTER = "Product Details List"

        const val TYPE_WISH_LIST_ADAPTER = "Wish List"
        /*CATEGORY DATA*/
        const val KEY_CAT_DATA = "KEY_CAT_DATA"
        const val KEY_PROD_ID = "KEY_PROD_ID"
        const val SUBCAT_ID = "subcat_id"
        const val PRODUCT_DETAIL_RESP = "product_details_resp"
        /*Landing Activity Constants*/
        val SOURCE_SIGN = "Source Sign"
        val EXTRA_SOCIAL_INPUT = "Social input"

        /*CHANGE PSSWRD*/
        const val SOURCE_CHANGE_PSSWRD = "source change password"
        const val SOURCE_OTP = "source otp"

        /*MY ACCOUNT*/
        const val MY_ACCOUNT = "source myaccount"

        /*MY ACCOUNT*/
        const val GENDER_SIGNUP = "Gender"

        /*HOME*/
        const val _SESION = "_session"
        const val HOME_SCREEN = "home screen"
        const val OFFERS_SCREEN = "offers screen"

        const val NAVIGATION_FRAGMENT = "NAVIGATION FRAGMENT"
        const val SUB_BASE = "sub base screen"
        const val ORDER_NOW_BOTTOM_SHEET_FRAGMENT = "OrderNowBottomSheetFragment"
        const val PRE_ORDER_NOW_BOTTOM_SHEET_FRAGMENT = "PreOrderNowBottomSheetFragment"
        const val LANDING_SCREEN = "Landing screen"
        const val LOGIN_SCREEN = "Login screen"
        const val OTP_SCREEN = "Otp screen"
        const val SPLASH_SCREEN = "Splash screen"
        const val MY_ACCOUNT_SCREEN = "my accont screen"
        const val ORDER_TYPE = "order_type"
        const val DELEVERY_TYPE = "delivery_type"


        const val SHOP_BY_CATEGORY = "shop by category"

        val SOURCE_HOME = "Source Home"
        /*Sub Category*/
        const val ID_CATEGORY = "id_category"
        const val PARENT_CATE_NAME = "parent cate name "
        /*SHOP BY CATEGORY*/
        const val EXTRA_CATEGORY_ID = "category id"

        /*Wish LIst*/
        const val WISHLIST_GET = "get"
        const val WISHLIST_CREATE = "create"
        const val WISHLIST_DELETE = "delete"
        /*PRODUCT LIST ADAPTER*/

        const val OPERATION = "operation"
        const val ADDRESS_TYPE_ADD = "address_type_add"
        const val ADDRESS_TYPE_UDATE = "address_type_udate"
        const val ADDRESS_DATA = "address_data"

        const val OP_PROD_DETAILS = "OP_PROD_DETAILS"
        const val OP_UPDATE_ADDRESS = "OP_UPDATE_ADDRESS"
        const val OP_SET_DEF_ADDRESS = "op_set_def_address"
        const val OP_MODIFY_CART = "modify_cart"
        const val OP_ADD_CART = "add_cart"
        const val CART_CREATE = "cart create"

        const val OP_REMOVE_FROM_CART = "remove_from_cart"
        const val OP_REMOVE_ALL_CART = "remove_all_cart"
        const val RES_SUCCESS = "success"
        const val RES_FAILED = "failed"
        const val ID_SUB_CATEGORY = "id_subcategory"
        const val WISHLIST = "Wishlist"
        const val TABLE = "table"
        const val CITY = "city"
        const val ALL = "all"
        const val CREATE = "create"
        const val GET = "get"
        const val UPDATE = "update"


        const val PRICE = "Price"
        const val SORT = "refineValues"
        const val START = "start"
        const val COUNT = "count"
        const val EXPRESS = "express"
        const val PARENT_CAT_ID = "parent id"
        const val PARENT_CAT_NAME = "parent name"
        const val MODIFY = "modify"
        const val PRODUCT_WISH_LIST_CHANGED = "PRODUCT_WISH_LIST"
        const val POS = "pos"
        const val ID_PRODUCT = "id_product"
        const val TEMP_CART_VALUE = "temp_cart_value"
        const val CART_DATA_PREF = "CART_DATA_PREF"
        const val ERROR_CODE_100 = "100"
        val ADD_ADDRESS = 7
        val EDIT_ADDRESS = 8
        const val DELIVERY_ADDRESS = "delivery address"

        const val SOURCE_SELECT_ADDRESS = "SelectAddress Activity"
        const val SOURCE_LOCATION_ACTIVITY = "Location Activity"
        const val SOURCE_DELIVERY_ADDRESS_ACTIVITY = "Delivery Address Activity"

        val LOCATION_SELECTOR = 9

        const val ID_CUSTOMER = "id_customer"
        const val ID_ORDER_NUM= "order_no"
        const val ORDERS_STATUS = "order_status"

        const val TYPE_COMBO_LIST_ADAPTER = "Combo List"



        const val LIST_ADDRESS = "list_address"
        const val DELETE = "delete"
        const val Id_ADDRESS = "id_address"
        const val SELECTED = "selected"
        const val EDIT = "edit"
        const val SOURCE_EDITADDRESS = "EDITADDRESS"
        const val UPDATE_ADDRESS_DATA = "address data"
        const val DELETE_CONFIRMATION = "delete confirmation"
        const val REQUEST_CHECK_SETTINGS = 12
        const val ADDRESS_LOC = "address_location"
        const val REG_LOC = "add_loc"

        const val ORDER_CANCELLED_CONFIRMATION = "cancel order confirmation"
        const val ORDER_RETURNED_CONFIRMATION = "return order confirmation"


        const val FAQ = "faq"

        /*OFFERS*/
        const val TYPE_OFFERS_ADAPTER = "offer adapter"

        /*OFFERS DETAIL*/
        const val TYPE_OFFERS_DETAILS_ADAPTER = "offer detail adapter"

        /*SMART BASKET*/
        const val TYPE_SMART_BASKET_ADAPTER = "basket adapter"



        /*PAYMENT INPUTS*/
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val CURRENCY = "currency"
        const val AMOUNT = "amount"
        const val EMAIL_PAYMENT = "email"
        const val CONTACT = "contact"

        /*CART SUMMARY */
        const val COUPON_CODE_="coupon_code"
        const val PAYMENT_TPE_COD="cod"
        const val PAYMENT_TPE_ONLINE="online"
        const val SOURCE_CART_SUMMARY = "source Cart summary"
        const val PAYMENT_TPE = "payment type"
        const val CART_SUMMARY = "Cart summary"
        const val TYPE_CART_SUMMARY= "Cart summary Adapter"
        const val TYPE_DELIVERY_SLOT= "Delivery slot Adapter"
        const val DELIVERY_TYPE_SELECTION= "Delivery selection"
        const val DELIVERY_SELCTED_TIME_SLOT= "Delivery Time slot"
        const val IS_EXPRESS= "is_express"

        const val REFINE_BY = "Refine By"
        const val TYPE_FILTER_ADAPTER = "Filter Type Adapter"
        const val TYPE_RIFINED_LIST = "Refined List"

        const val BRAND = "Brand"
        const val OFFER = "Offer"



        /*PAYMENT INPUTS*/
        const val ID_ORDER= "id_order"
        const val TOTAL_PAID= "total_paid"
        const val DELIVERY_SLOT= "delivery_slot"
        const val PAY_TYPE= "pay_type"
        const val WALLET_AMT= "wallet_amount"
        const val PAYMENT_EXPRESS= "express"
        const val ORDERS= "orders"


        const val ORDER_NO = "order_no"
        const val ORDER_STATUS = "order_status"
        const val ORDER_CANCELLED = "Cancelled"
        const val ORDER_RETURNED = "Returned"


        const val ID_SKU= "id_sku"
        const val RETURNED_QUANTITY= "returned_quantity"
        const val RETURN_ITEMS= "return_items"
        const val ORDER_ID= "order_id"
        const val RETURN_REASON= "return_reason"
        const val RETURN_REQUEST= "Return Request"
        const val RETURN_STOCK_TYPE= "return_stock_type"
        const val RETURN_STOCK_TYPE_NUM= "1"

        const val TRACK_ORDER = "track_order"



        /*FIREBASE SETUP*/

        const val FIREBASE_TOKEN = "Firebase toke id"
        const val FIREBASE_BODY = "message"
        const val FIREBASE_IMAGE = "image"
        const val FIREBASE_TITLE = "title"
        const val APP = "app"
        const val DEVICE = "device"
        const val FCM_TOKEN = "fcm_token"
        const val DEVICE_ID = "deviceid"
        const val FIREBASE_MSG = "m"


        const val CUR_CUSTOMER = "customer "
        const val CHECKOUT_ADDRESS = "checkout_address "
        val ID_OFFER: String?="id_offer"


    }
}