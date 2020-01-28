package com.freshbasket.customer.util

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.freshbasket.customer.MyApplication.Companion.context
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.listner.IDialogClickListener
import com.freshbasket.customer.listner.ISelectedDateListener
import com.freshbasket.customer.model.CartSummaryResp
import com.freshbasket.customer.model.Category
import com.freshbasket.customer.model.Customer
import com.freshbasket.customer.model.Product
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.screens.login.LoginActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.custome_toast.view.*
import kotlinx.android.synthetic.main.partail_popup_dialog.view.*
import kotlinx.android.synthetic.main.partail_return_popup_dialog.view.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class CommonUtils {
    companion object {
        private var dateListener: ISelectedDateListener? = null
        private var TAG: String = "COMMON UTILS"
        private var myProgressDialog: ProgressDialog? = null
        fun showLoading(mContext: Context, cancelable: Boolean = false) {
            try {
                hideLoading()
                myProgressDialog = ProgressDialog(mContext, R.style.AppTheme_Loading_Dialog)
                myProgressDialog?.apply {
                    setMessage(mContext.getString(R.string.please_wait))
                    setCancelable(true)
                    setOnCancelListener {
                        dismiss()
                    }
                    show()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        fun hideLoading() {
            myProgressDialog?.apply {
                if (isShowing) {
                    dismiss()
                    myProgressDialog = null
                }
            }
        }


        /*CALENDER */
        fun showCalenderPicker(context: Context, listener: ISelectedDateListener, min: Long = 0, max: Long = 0) {
            dateListener = listener
            val mYear: Int
            val mMonth: Int
            val mDay: Int
            val cal = Calendar.getInstance()
            mYear = cal.get(Calendar.YEAR)
            mMonth = cal.get(Calendar.MONTH)
            mDay = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                    context, R.style.MyDatePickerDialogTheme,
                    { view, year, monthOfYear, dayOfMonth ->
                        val selCal = Calendar.getInstance()
                        selCal.timeInMillis = 0
                        selCal.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
                        listener.setSelectedDate(String.format(" %1\$tb %1\$td,%1\$tY", selCal))
                    }, mYear, mMonth, mDay

            )

            if (min > 0)
                datePickerDialog.datePicker.minDate = min
            else
                datePickerDialog.datePicker.minDate = cal.timeInMillis
            if (max > 0)
                datePickerDialog.datePicker.maxDate = max
            /*else
                datePickerDialog.datePicker.maxDate = cal.timeInMillis*/
            datePickerDialog.setTitle("")
            datePickerDialog.show()


        }

        fun startActivity(mContext: Context, activity: Class<*>, bundle: Bundle, actfinish: Boolean) {
            val move = Intent(mContext, activity)
            move.putExtras(bundle)
            mContext.startActivity(move)
            if (mContext is Activity) {
                if (actfinish)
                    mContext.finish()
            }
        }

        fun shareApplication(context: Context, share_message: String) {
            try {
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FreshBasket")
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share_message)
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }

        fun setAuthorizationkey(key: String?) {
            MyLogUtils.d(TAG, "Authorization key : $key")
            SharedPreferenceManager.setPrefVal(Constants.AUTHORIZATION_KEY, key!!, SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getAutharizationKey(): String {
            return SharedPreferenceManager.getPrefVal(Constants.AUTHORIZATION_KEY, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun getSession(): String {
            val session = SharedPreferenceManager.getPrefVal(Constants._SESION, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            if (session.isEmpty()) {
                return generateSession()
            }
            return session
        }

        private fun generateSession(): String {
            return try {
                val chars = "abcdefghijklmnopqrstuvwxyz".toCharArray()
                val sb = StringBuilder()
                val random = Random()
                for (i in 0..15) {
                    val c = chars[random.nextInt(chars.size)]
                    sb.append(c)
                }
                val randomString = sb.toString() + "_" + SimpleDateFormat("ddMMyyyyhhmmssSSS").format(java.util.Date())
                SharedPreferenceManager.setPrefVal(Constants._SESION, randomString, SharedPreferenceManager.VALUE_TYPE.STRING)
                randomString
            } catch (ex: Exception) {
                ex.printStackTrace()
                return ""
            }
        }


        fun setCustomerData(customerData: Customer) {
            try {

                setUserLogin(true)
                setCurrentUser(customerData)
                if (Validation.validateValue(customerData._id)) {
                    SharedPreferenceManager.setPrefVal(Constants.CUSTOMER_ID, customerData._id
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.AADHAR_PIC, customerData.aadhar_pic
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.APP_VERSION, customerData.app_version
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)

                    SharedPreferenceManager.apply {
                        setPrefVal(Constants.CUSTOMER_ID, customerData._id
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.AADHAR_PIC, customerData.aadhar_pic
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.APP_VERSION, customerData.app_version
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.BANK_ACCOUNT_NUM, customerData.bank_acc_number!!, SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.BIRTHDAY, customerData.birthday
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.CUSTOMER_TYPE, customerData.customer_type
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.EMAIL, customerData.email
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.EMAIL_VERIFIED, customerData.email_verified
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.FACEBOOK_ID, customerData.facebook_id
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.FIRST_NAME, customerData.first_name
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.GOOGLE_ID, customerData.google_id
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.GENDER, customerData.gender
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.IFSC_CODE, customerData.ifsc_code
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.LAST_NAME, customerData.last_name
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.LINKEDIN_ID, customerData.linkedin_id
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.MOBILE, customerData.mobile
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.NEWS_LETTER, customerData.news_letter
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.NOTE, customerData.note
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.OTP, customerData.otp
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.PANCARD_PIC, customerData.pancard_pic
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.PIC, customerData.pic
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.REFERRAL_APPLIED, customerData.referral_applied
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.REFERRAL_CODE, customerData.referral_code
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.REFERRAL_USED, customerData.referral_used
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.REFERRAL_AMOUNT, customerData.referred_amount
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.REFERRED_BY, customerData.referred_by
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.REFERRED_CODE, customerData.referred_code
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.VERIFIED, customerData.verified
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.WALLET, customerData.wallet
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                        setPrefVal(Constants.WEBSITE, customerData.website
                                ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)

                    }
                    SharedPreferenceManager.setPrefVal(Constants.CUSTOMER_ID, customerData._id
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.AADHAR_PIC, customerData.aadhar_pic
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.APP_VERSION, customerData.app_version
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.BANK_ACCOUNT_NUM, customerData.bank_acc_number!!, SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.BIRTHDAY, customerData.birthday
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.CUSTOMER_TYPE, customerData.customer_type
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.EMAIL, customerData.email
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.EMAIL_VERIFIED, customerData.email_verified
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.FACEBOOK_ID, customerData.facebook_id
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.FIRST_NAME, customerData.first_name
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.GOOGLE_ID, customerData.google_id
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.GENDER, customerData.gender
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.IFSC_CODE, customerData.ifsc_code
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.LAST_NAME, customerData.last_name
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.LINKEDIN_ID, customerData.linkedin_id
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.MOBILE, customerData.mobile
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.NEWS_LETTER, customerData.news_letter
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.NOTE, customerData.note
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.OTP, customerData.otp
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.PANCARD_PIC, customerData.pancard_pic
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.PIC, customerData.pic
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.REFERRAL_APPLIED, customerData.referral_applied
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.REFERRAL_CODE, customerData.referral_code
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.REFERRAL_USED, customerData.referral_used
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.REFERRAL_AMOUNT, customerData.referred_amount
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.REFERRED_BY, customerData.referred_by
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.REFERRED_CODE, customerData.referred_code
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.VERIFIED, customerData.verified
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.WALLET, customerData.wallet
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)
                    SharedPreferenceManager.setPrefVal(Constants.WEBSITE, customerData.website
                            ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }

        fun setUserLogin(isLogin: Boolean) {
            SharedPreferenceManager.setPrefVal(Constants.IS_USER_LOGIN, isLogin, SharedPreferenceManager.VALUE_TYPE.BOOLEAN)
        }

        fun isUserLogin(): Boolean {
            return SharedPreferenceManager.getPrefVal(Constants.IS_USER_LOGIN, false, SharedPreferenceManager.VALUE_TYPE.BOOLEAN) as Boolean
        }


        fun getEmailId(): String {
            return SharedPreferenceManager.getPrefVal(Constants.EMAIL, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }


        fun getOTP(): String {
            return SharedPreferenceManager.getPrefVal(Constants.OTP, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun getCustomerID(): String {
            return SharedPreferenceManager.getPrefVal(Constants.CUSTOMER_ID, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun getCustomerFirstName(): String {
            return SharedPreferenceManager.getPrefVal(Constants.FIRST_NAME, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun getCustomerPic(): String {
            return SharedPreferenceManager.getPrefVal(Constants.PIC, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun getCustomerPassword(): String {
            return SharedPreferenceManager.getPrefVal(Constants.PASSWORD, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun setCustomerPassword(pass: String) {
            SharedPreferenceManager.setPrefVal(Constants.PASSWORD, pass, SharedPreferenceManager.VALUE_TYPE.STRING)
        }

        fun getCustomerMobileNumber(): String {
            return SharedPreferenceManager.getPrefVal(Constants.MOBILE, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun getProfilePic(): String {
            return SharedPreferenceManager.getPrefVal(Constants.PIC, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
        }

        fun saveCurrentLocation(address: Address?) {
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.PREF_LOC, Gson().toJson(address), SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getCurrentLocation(): Address? {
            val json = SharedPreferenceManager.getPrefVal(SharedPreferenceManager.PREF_LOC, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            return try {
                Gson().fromJson<Address>(json, Address::class.java) as Address
            } catch (exp: java.lang.Exception) {
                null
            }
        }

        fun getCurrentLocationAddress(context: Context, latitude: Double, longitude: Double): Address? {
            var fetchedAddress: Address? = null
            val geocoder = Geocoder(context, Locale.ENGLISH)
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                if (addresses.size > 0) {
                    fetchedAddress = addresses[0]
                    val strAddress = StringBuilder()
                    for (i in 0 until fetchedAddress!!.maxAddressLineIndex) {
                        strAddress.append(fetchedAddress.getAddressLine(i)).append(" ")
                    }

                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return fetchedAddress
        }

        fun saveMyAddress(address: Address?) {
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.MY_ADDRESS, Gson().toJson(address), SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getMyAddress(): Address? {
            val json = SharedPreferenceManager.getPrefVal(SharedPreferenceManager.MY_ADDRESS, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            return try {
                Gson().fromJson<Address>(json, Address::class.java) as Address
            } catch (exp: java.lang.Exception) {
                null
            }
        }

        fun showCustomToast(context: Context, msg: String, length: Int) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater.inflate(R.layout.custome_toast, null) as View
            val toast = Toast(context)
            toast.setGravity(Gravity.BOTTOM, 0, 0)
            toast.duration = length
            toast.view = layout
            toast.show()
            if (msg != null)
                layout.tv_toast_msg.text = msg
            else
                layout.tv_toast_msg.text = context.getString(R.string.Please_login_to_the_application)
        }

        fun showDialog(context: Context, body: String) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partail_popup_dialog, null)

            dialogview.dialog_body.text = body
            builder.setView(dialogview)
            val dialog = builder.create()
            dialogview.btn_ok.setOnClickListener {
                dialog.dismiss()
                SharedPreferenceManager.clearPreferences()
                val bundle = Bundle()
                startActivity(context, LoginActivity::class.java, bundle, true)
            }

            dialogview.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()


        }

        fun showConfirmationDialog(context: Context, listener: IAdapterClickListener, body: String, item: Any?, pos: Int) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partail_popup_dialog, null)

            dialogview.dialog_body.text = body
            builder.setView(dialogview)

            val dialog = builder.create()
            dialogview.btn_ok.setOnClickListener {
                dialog.dismiss()
                listener.onclick(item = item!!, pos = pos, type = Constants.DELETE_CONFIRMATION)

            }
            dialogview.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }


        fun showConfirmationDialogPopup(context: Context, listener: IAdapterClickListener, body: String, item: Any?, pos: Int) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partail_popup_dialog, null)

            dialogview.dialog_body.text = body
            builder.setView(dialogview)

            val dialog = builder.create()
            dialogview.btn_ok.setOnClickListener {
                dialog.dismiss()
                listener.onclick(item = item!!, pos = pos, op = Constants.ORDER_CANCELLED_CONFIRMATION)
            }
            dialogview.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }


        fun showRetunConfirmationDialogPopup(context: Context, listener: ISelectedDateListener, body: String, item: Any?, pos: Int) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partail_return_popup_dialog, null)

            dialogview.return_dialog_body.text = body
            builder.setView(dialogview)

            val dialog = builder.create()
            dialogview.return_btn_ok.setOnClickListener {
                dialog.dismiss()
                val reason_text = dialogview.findViewById<EditText>(R.id.ed_return_reason)
                listener.setSelectedDate(reason_text.text.toString())


            }
            dialogview.return_btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }


        /*SKU DIALOG*/

        fun showSkuDialog(context: Context, data: Product, dialogClickListener: IDialogClickListener) {
            val skuArrayList = data.sku
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dailogview = inflater.inflate(R.layout.dialog_sku_popup_quantity, null)
            val dynamicLayout = dailogview.findViewById(R.id.dynamic_layout) as LinearLayout
            val productName = dailogview.findViewById(R.id.product_name) as TextView
            if (!data.name.isNullOrBlank())
                productName.text = data.name
            builder.setView(dailogview)
            val dialog = builder.create()

            dynamicLayout.removeAllViews()
            for (sku in skuArrayList!!) {
                val childLayout = LayoutInflater.from(context).inflate(R.layout.sku_quantity_item, null)
                val title = childLayout.findViewById(R.id.tv_quantity_name) as TextView
                val checkBx = childLayout.findViewById(R.id.cb_selected) as RadioButton

                checkBx.isChecked = sku?._id == data.getSelectedSku()?._id

                if (!sku?.size.isNullOrBlank())
                    title.text = sku?.size
                childLayout.setOnClickListener {
                    data.selSku = sku
                    data.selSku?.tempMyCart = -1
                    checkBx.isChecked = true
                    dialogClickListener.selectedItem(sku)
                    dialog.cancel()
                }
                dynamicLayout.addView(childLayout)
            }
            dialog.show()
        }

        fun convertToBase64(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val byteArrayImage = baos.toByteArray()
            return Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
        }

        fun getParentCategoryData(type: String?): Category? {
            type?.apply {
                val mainCatlist = SharedPreferenceManager.getCategoryListData()
                if (Validation.isValidList(mainCatlist)) {
                    for (cur in mainCatlist!!) {
                        if (type.contentEquals(cur._id!!))
                            return cur
                    }
                }
            }
            return null
        }

        fun getRupeesSymbol(context: Context, price: String): String {
            return context.getString(R.string.Rs) + price
        }


        fun saveOtpVerificationHashKey(otpStaticKey: String) {
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.OTP_VERIFICATION_KEY, otpStaticKey, SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getOtpVerificationHashKey(): String {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.OTP_VERIFICATION_KEY, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

        fun setCustomerEmailMobileNumber(customercare_email: String, customercare_mobile: String) {
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.CUSTOMER_EMAIL, customercare_email, SharedPreferenceManager.VALUE_TYPE.STRING)
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.CUSTOMER_MOBILE_NUMBER, customercare_mobile, SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getCustomerEmail(): String {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.CUSTOMER_EMAIL, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

        fun getCustomerCareMobileNumber(): String {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.CUSTOMER_MOBILE_NUMBER, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

        fun setShareMessage(share_message: String, refferal_code: String) {
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.SHARE_MSG, share_message, SharedPreferenceManager.VALUE_TYPE.STRING)
            SharedPreferenceManager.setPrefVal(SharedPreferenceManager.REFERRAL_CODE, refferal_code, SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getSharedMSg(): String {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.SHARE_MSG, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

        fun getReferralCode(): String {
            return SharedPreferenceManager.getPrefVal(SharedPreferenceManager.REFERRAL_CODE, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

        fun saveCartSummaryResp(cartsummary: CartSummaryResp?) {
            SharedPreferenceManager.setPrefVal(Constants.CART_SUMMARY, Gson().toJson(cartsummary), SharedPreferenceManager.VALUE_TYPE.STRING)
        }


        fun getCartSummaryResp(): CartSummaryResp? {
            val json = SharedPreferenceManager.getPrefVal(Constants.CART_SUMMARY, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            return try {
                Gson().fromJson<CartSummaryResp>(json, CartSummaryResp::class.java) as CartSummaryResp
            } catch (exp: Exception) {
                null
            }
        }


        fun getPaymentJsonParameter(cartSummaryResp: CartSummaryResp): JSONObject {
            val jsonObject = JSONObject()
            jsonObject.put(Constants.NAME, CommonUtils.getCustomerFirstName())
            jsonObject.put(Constants.DESCRIPTION, "ORDER ID : ".plus(cartSummaryResp.orders!![0].order_id.toString()))
            jsonObject.put(Constants.CURRENCY, "INR")
            jsonObject.put(Constants.AMOUNT, cartSummaryResp.summary!!.grand_total)
            //jsonObject.put(Constants.AMOUNT, "100")
            jsonObject.put(Constants.EMAIL_PAYMENT, CommonUtils.getEmailId())
            jsonObject.put(Constants.CONTACT, CommonUtils.getCustomerMobileNumber())
            return jsonObject
        }


        fun setDeliverySelectionMethod(deleveryMethod: Boolean) {
            SharedPreferenceManager.setPrefVal(Constants.DELIVERY_TYPE_SELECTION, deleveryMethod, SharedPreferenceManager.VALUE_TYPE.BOOLEAN)

        }

        fun getDeliverySelectedMethod(): String {
            val dMethod = SharedPreferenceManager.getPrefVal(Constants.DELIVERY_TYPE_SELECTION, false, SharedPreferenceManager.VALUE_TYPE.BOOLEAN) as Boolean
            if (dMethod)
                return "1"
            return "0"

        }


        fun setSelectedTimeslot(deleveryMethod: String) {
            SharedPreferenceManager.setPrefVal(Constants.DELIVERY_SELCTED_TIME_SLOT, deleveryMethod, SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getDeliverySelectedTimeSlot(): String {
            return SharedPreferenceManager.getPrefVal(Constants.DELIVERY_SELCTED_TIME_SLOT, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

        /*AFTER CHECK OUT CLEAR THE SHARED PREFERENCE DATA*/
        fun clearSharedPrefernceData() {
            setSelectedTimeslot("")
            setDeliverySelectionMethod(false)
        }


        fun paymentConfirmationDialog(context: Context, body: String) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val dialogview = inflater.inflate(R.layout.partail_popup_dialog, null)
            builder.setCancelable(true)
            dialogview.dialog_body.text = body
            builder.setView(dialogview)
            dialogview.btn_cancel.visibility = View.GONE

            val dialog = builder.create()
            dialogview.btn_ok.setOnClickListener {
                dialog.dismiss()
                navigateHomeScreen(context)
            }

            dialog.show()
        }

        private fun navigateHomeScreen(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.apply {
                putExtra(Constants.SOURCE, Constants.LANDING_SCREEN)
            }
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
        }

        fun saveFirebaseToken(deleveryMethod: String) {
            SharedPreferenceManager.setPrefVal(Constants.FIREBASE_TOKEN, deleveryMethod, SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getFireBaseToken(): String {
            return SharedPreferenceManager.getPrefVal(Constants.FIREBASE_TOKEN, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

        fun getDeviceID(): String {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
        }

        fun getBitMapURL(imageURL: String): Bitmap? {

            try {
                val url = URL(imageURL)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream

                return BitmapFactory.decodeStream(input)

            } catch (e: Exception) {
                e.printStackTrace()
                return null

            }

        }


        fun setCurrentUser(customer: Customer?) {
            SharedPreferenceManager.setPrefVal(Constants.CUR_CUSTOMER, Gson().toJson(customer), SharedPreferenceManager.VALUE_TYPE.STRING)
        }


        fun getCurrentUser(): Customer? {
            val json = SharedPreferenceManager.getPrefVal(Constants.CUR_CUSTOMER, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String
            return try {
                Gson().fromJson<Customer>(json, Customer::class.java) as Customer
            } catch (exp: Exception) {
                null
            }
        }

        fun goToDialPad(context: Context, mobileNumber: String) {
            try {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$mobileNumber")
                context.startActivity(intent)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }


        fun goToEmailApp(context: Context, emailId: String, subject: String) {
            try {
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailId, null))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
                emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }

        fun saveDeliveryAddress(address: String?) {
            SharedPreferenceManager.setPrefVal(Constants.DELIVERY_ADDRESS, address
                    ?: "", SharedPreferenceManager.VALUE_TYPE.STRING)

        }

        fun getDeliveryAddress(): String {
            return SharedPreferenceManager.getPrefVal(Constants.DELIVERY_ADDRESS, "", SharedPreferenceManager.VALUE_TYPE.STRING) as String

        }

    }
}


