package com.freshbasket.customer.api

import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.util.CommonUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject

object ApiRequestParam {
    var respParamObj = JsonObject()


    fun getCommonParameter(jsonObject: JsonObject) {
        jsonObject.addProperty(Constants.CUSTOMER_ID, CommonUtils.getCustomerID())
    }

    fun getSession(jsonObject: JsonObject) {
        jsonObject.addProperty(Constants._SESION, CommonUtils.getSession())
    }

    /*LOGIN SCREEN */
    fun login(email: String, password: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.email, email)
            addProperty(Constants.password, password)
        }
        return respParamObj
    }

    /*Forgot Password*/
    fun getforgotPasswrd(name: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.email, name)
            addProperty(Constants.SMS_KEY, CommonUtils.getOtpVerificationHashKey())
        }
        return respParamObj
    }

    /*Notification*/
    fun getNotificationData(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.ID_CUSTOMER, CommonUtils.getCustomerID())

        }
        return respParamObj
    }


    /*ReferAndEarn*/
    fun getReferAndEarnData(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.addProperty(Constants.CUSTOMER_ID, CommonUtils.getCustomerID())
        return respParamObj
    }

    /*VERIFY OTP*/

    fun verifyOtp(otp: String, email_id: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            addProperty(Constants.OTP, otp)
            addProperty(Constants.EMAIL, email_id)

        }
        return respParamObj
    }


    /*CHANGE PSSWRD*/

    fun changePassword(changed_password: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getCommonParameter(respParamObj)
            addProperty(Constants.password, changed_password)
        }
        return respParamObj
    }

    /*HOME API*/
    fun getHomeParameters(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            if (CommonUtils.isUserLogin())
                getCommonParameter(respParamObj)
            getSession(respParamObj)
            addProperty(Constants.DEVICE_TYPE, Constants.ANDROID)

        }
        return respParamObj
    }

    /*HHOME API*/
    fun getFireBaseToken(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getCommonParameter(respParamObj)
            //addProperty(Constants.DEVICE_TYPE, Constants.ANDROID)
            addProperty(Constants.FCM_TOKEN, CommonUtils.getFireBaseToken())
            addProperty(Constants.DEVICE_ID, CommonUtils.getDeviceID())
            addProperty(Constants.DEVICE, Constants.ANDROID)
            addProperty(Constants.APP, Constants.APP_TYPE_CUSTOMER)

        }
        return respParamObj
    }

    /*SHOP BY CATEGORY*/
    fun getsubCategoryParameters(id: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.ID_CATEGORY, id)
        }
        return respParamObj
    }

    fun getCartParameters(op: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            when (op) {
                Constants.CART_GET -> {
                    getSession(respParamObj)
                    getCommonParameter(respParamObj)
                    respParamObj.addProperty(Constants.OP, Constants.CART_GET)

                }
            }
        }
        return respParamObj
    }

    fun getProductListParameters(catId: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.CUSTOMER_ID, catId) ///id cate id

        }
        return respParamObj
    }

    fun getProductDeatilsParameters(product_id:String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getSession(respParamObj)
            getCommonParameter(respParamObj)
            respParamObj.addProperty("products", product_id)

        }
        return respParamObj
    }


    fun getChildProductListing(catId: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getCommonParameter(respParamObj)
            getSession(respParamObj)
            respParamObj.addProperty(Constants.ID_SUB_CATEGORY, catId)
            respParamObj.addProperty(Constants.PRICE, "")
            respParamObj.addProperty(Constants.SORT, "")
            respParamObj.addProperty(Constants.COUNT, 50) ///id cate id
            respParamObj.addProperty(Constants.EXPRESS, 0)
        }
        return respParamObj
    }

    fun getMyAccountParameters(first_name: String, email: String, pic: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getCommonParameter(respParamObj)
            respParamObj.addProperty(Constants.FIRST_NAME, first_name)
            respParamObj.addProperty(Constants.EMAIL, email)
            if (pic.isNotEmpty())
                respParamObj.addProperty(Constants.PIC_DATA, pic)
        }
        return respParamObj
    }


    fun getMyOrderParameters(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getCommonParameter(respParamObj)

        }
        return respParamObj
    }


    fun getOffersParameters(_id: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.CUSTOMER_ID, _id)

        }
        return respParamObj
    }

    fun geCancelOrderParameters(customer_id: String, order_no: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.CUSTOMER_ID, customer_id)
            respParamObj.addProperty(Constants.ORDER_NO, order_no)
            respParamObj.addProperty(Constants.ORDER_STATUS, Constants.ORDER_CANCELLED)
        }
        return respParamObj
    }


    fun geReturnOrderParameters(customer_id: String, order_id: String, return_reason: String, jsonArray: JsonArray): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.CUSTOMER_ID, customer_id)
            respParamObj.addProperty(Constants.ORDER_ID, order_id)
            respParamObj.addProperty(Constants.RETURN_REASON, return_reason)
            respParamObj.addProperty(Constants.status, Constants.RETURN_REQUEST)
            respParamObj.addProperty(Constants.RETURN_STOCK_TYPE, Constants.RETURN_STOCK_TYPE_NUM)
            respParamObj.add(Constants.RETURN_ITEMS, jsonArray)

        }
        return respParamObj
    }

    fun getMyOrderDetailsParameters(order_no: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getCommonParameter(respParamObj)
            respParamObj.addProperty(Constants.ID_ORDER_NUM, order_no)

        }
        return respParamObj
    }

    /*SELECT ADDRESS*/
    fun getSelectAddressParameters(op: String, customer_id: String, address_id: String, selected: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.OP, op)
            respParamObj.addProperty(Constants.ID_CUSTOMER, customer_id)
            respParamObj.addProperty(Constants.Id_ADDRESS, address_id)
            respParamObj.addProperty(Constants.SELECTED, selected)
        }
        return respParamObj
    }

    /*FILTER*/
    fun getFilterParameters(id: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.CUSTOMER_ID, CommonUtils.getCustomerID())
            getSession(respParamObj)
            respParamObj.addProperty(Constants.ID_CATEGORY, id)
        }
        return respParamObj
    }

    /*MY WALLET*/
    fun getMyWalletParameters(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {

        }
        return respParamObj
    }

    /*CHECKOUT*/
    fun getCheckoutParameters(jsonArray: JsonArray): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.add(Constants.ORDERS, jsonArray)
            getCommonParameter(respParamObj)
        }
        return respParamObj
    }

    /*WISH LIST*/
    fun getWishListParameters(op: String, productId: String): JsonObject {
        respParamObj = JsonObject()
        getCommonParameter(respParamObj)
        getSession(respParamObj)
        respParamObj.apply {
            when (op) {
                Constants.WISHLIST_GET ->
                    respParamObj.addProperty(Constants.OP, op)

                Constants.WISHLIST_CREATE -> {
                    respParamObj.addProperty(Constants.ID_PRODUCT, productId)
                    respParamObj.addProperty(Constants.OP, op)
                }

                Constants.WISHLIST_DELETE -> {
                    respParamObj.addProperty(Constants.ID_PRODUCT, productId)
                    respParamObj.addProperty(Constants.OP, op)
                }

            }
        }
        return respParamObj
    }

    fun getLocality(op: String, city: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.OP, op)
            respParamObj.addProperty(Constants.TABLE, city)

        }
        return respParamObj

    }

    fun getAddressList(op: String, customer_id: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.OP, op)
            getCommonParameter(respParamObj)
            respParamObj.addProperty(Constants.ID_CUSTOMER, customer_id)

        }
        return respParamObj
    }

    fun deleteAddress(op: String, address_id: String, customer_id: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.OP, op)
            addProperty(Constants.Id_ADDRESS, address_id)
            respParamObj.addProperty(Constants.ID_CUSTOMER, customer_id)
        }
        return respParamObj
    }

    fun getFaqList(op: String, table: String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.OP, op)
            respParamObj.addProperty(Constants.TABLE, table)
        }
        return respParamObj
    }


    fun getCartSummaryReqParameter(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            // respParamObj.addProperty(Constants.COUPON_CODE_, couponCode)
            getSession(respParamObj)
            respParamObj.addProperty(Constants.IS_EXPRESS, CommonUtils.getDeliverySelectedMethod())

        }
        return respParamObj
    }

    fun getCouponListReqParameter(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getSession(respParamObj)
            getCommonParameter(respParamObj)
        }
        return respParamObj
    }

    fun getComboDetailsParameters(cat_id :String): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            respParamObj.addProperty(Constants.ID_CUSTOMER, CommonUtils.getCustomerID())
            respParamObj.addProperty(Constants.CUSTOMER_ID,cat_id)
            getSession(respParamObj)
        }
        return respParamObj
    }

    fun getComboParameters(): JsonObject {
        respParamObj = JsonObject()
        respParamObj.apply {
            getSession(respParamObj)
            respParamObj.addProperty(Constants.ID_CUSTOMER, CommonUtils.getCustomerID())
        }
        return respParamObj
    }
}