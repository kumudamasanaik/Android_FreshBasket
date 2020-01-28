package com.freshbasket.customer.api

import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.model.*
import com.freshbasket.customer.model.inputmodel.*
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST(ApiConstants.SIGN_UP)
    fun doRegister(@Body registerInput: SignUpInput): Call<CustomerRes>

    @POST(ApiConstants.LOGIN)
    fun doLogin(@Body json: JsonObject): Call<CustomerRes>

    @POST(ApiConstants.FORGOT_PASSWORD)
    fun doForgotPassword(@Body json: JsonObject): Call<CustomerRes>

    @POST(ApiConstants.NOTIFICATION)
    fun getNotification(@Body json: JsonObject): Call<NotificationResp>

    @POST(ApiConstants.SEARCH_PRODUCTS)
    fun getSearchResult(@Body input: SearchInput): Call<SearchProductRes>

    @POST(ApiConstants.VERIFY_OTP)
    fun verifyOtp(@Body json: JsonObject): Call<CustomerRes>

    @POST(ApiConstants.RESEND_OTP)
    fun resendOtp(@Body json: JsonObject): Call<CommonRes>

    @POST(ApiConstants.CHANGE_PASSWORD)
    fun doChangePassword(@Body json: JsonObject): Call<CustomerRes>

    @POST(ApiConstants.SOCIAL_LOGIN)
    fun doSocialLogin(@Body input: SocialSignInputModel): Call<CustomerRes>

    @POST(ApiConstants.REFER_AND_EARN)
    fun doReferAndEarn(@Body json: JsonObject): Call<CustomerRes>

    @POST(ApiConstants.HOME)
    fun getHomeApi(@Body json: JsonObject): Call<HomeResp>

    @POST(ApiConstants.UPDATE_FIREBASE_TOKEN_TO_SERVER)
    fun updateFirebaseTokeToServer(@Body json: JsonObject): Call<CommonRes>

    @GET(ApiConstants.GET_ALL_CATEGORY)
    fun getAllCategoryList(): Call<SubCategoryResp>


    @POST(ApiConstants.ADD_ADDRESS)
    fun getAddDeliveryAddress(@Body deliveryAddressInput: Address): Call<DeliveryAddRes>

    @POST(ApiConstants.SELECT_DELIVERY_ADDRESS)
    fun getSelectAddressApi(@Body json: JsonObject): Call<DeliveryAddRes>

    @POST(ApiConstants.UPDATEADDRESS)
    fun getUpdateAddress(@Body input: Address): Call<DeliveryAddRes>

    @POST(ApiConstants.DELADDRESS)
    fun getDeleteAddress(@Body json: JsonObject): Call<DeliveryAddRes>

    @POST(ApiConstants.MY_ORDER)
    fun getMyOrdersList(@Body jsonObject: JsonObject): Call<MyOrderResp>

    @POST(ApiConstants.CANCEL)
    fun getCancelOrder(@Body json: JsonObject): Call<CommonRes>


    @POST(ApiConstants.ORDER_RETURN)
    fun getReturnOrder(@Body json: JsonObject): Call<CommonRes>


    @GET(ApiConstants.TRACK_ORDER)
    fun doTrackOrder(): Call<CustomerRes>

    @POST(ApiConstants.ORDER_DETAILS)
    fun getOrderDetailsList(@Body json: JsonObject): Call<Order>

    @GET(ApiConstants.OFFERS)
    fun getOffers(): Call<OfferResp>

    @POST(ApiConstants.OFFERS_DETAILS)
    fun getOffersDetails(@Body json: JsonObject): Call<OrderDetailsResp>

    @POST(ApiConstants.FILTER)
    fun getFilter(@Body json: JsonObject): Call<NewFilterResUpdated>

    @POST(ApiConstants.MY_WALLET)
    fun getMyWallet(@Body json: JsonObject): Call<HomeResp>

    @POST(ApiConstants.CHECKOUT)
    fun doCheckout(@Body json: JsonObject): Call<HomeResp>


    @POST(ApiConstants.SMART_BASKET)
    fun getSmartBasketData(@Body json: JsonObject): Call<ComboDetailResp>

    @POST(ApiConstants.COMBO_LIST)
    fun getComboList(@Body json: JsonObject): Call<ComboOfferRes>

    @POST(ApiConstants.CART)
    fun getCart(@Body json: JsonObject): Call<FetchCartResp>

    @POST(ApiConstants.REMOVE_CART)
    fun removeCart(@Body json: ModifyCartIp): Call<FetchCartResp>


    @POST(ApiConstants.MYWISHLIST)
    fun getWishList(@Body json: JsonObject): Call<WishListResponse>


    @POST(ApiConstants.PRODUCT_LISTING)
    fun getALlProductList(@Body jsonObject: JsonObject): Call<ProductResp>

    @POST(ApiConstants.PRODUCT_LIST_HEADER)
    fun getChildCategoryForViewPageHeaders(@Body jsonObject: JsonObject): Call<ProductListHeaderResp>


    @POST(ApiConstants.PRODUCT_DETAILS)
    fun getProductDetails(@Body jsonObject: JsonObject): Call<ProductDetailsResp>

    @POST(ApiConstants.MY_ACCOUNT)
    fun getMyAccountList(@Body jsonObject: JsonObject): Call<MyAccountResp>


    @POST(ApiConstants.MODIFY_CART)
    fun getModifyCart(@Body jsonObject: ModifyCartIp): Call<FetchCartResp>


    @POST(ApiConstants.GET_LOCALITY)
    fun getLocality(@Body jsonObject: JsonObject): Call<LocalityRes>

    @POST(ApiConstants.COUPON_LIST)
    fun getCouponList(@Body jsonObject: JsonObject): Call<CouponRes>

    @POST(ApiConstants.APPLY_PROMO_CODE)
    fun applyPromoCode(@Body promoCodeInput: PromoCodeInput): Call<ApplyCouponRes>

    @POST(ApiConstants.ADDRESS_LIST)
    fun getAddressList(@Body json: JsonObject): Call<DeliveryAddRes>

    @POST(ApiConstants.FAQ)
    fun getFaqData(@Body json: JsonObject): Call<FaqRes>

    @POST(ApiConstants.CART_SUMMARY)
    fun getCartSummary(@Header(Constants.AUTHORIZATION)  header:String,@Body  jsonObject:JsonObject): Call<CartSummaryResp>

    @POST(ApiConstants.CHECKOUT)
    fun getCheckout( @Body jsonObject:JsonObject): Call<CheckOutResp>
}