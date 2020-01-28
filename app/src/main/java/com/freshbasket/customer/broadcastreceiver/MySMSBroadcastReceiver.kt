package com.freshbasket.customer.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class MySMSBroadcastReceiver : BroadcastReceiver() {

    var p = Pattern.compile("(|^)\\d{4}")
    private var otpReceiver: OTPReceiveListener? = null
    fun initOTPListener(receiver: OTPReceiveListener) {
        this.otpReceiver = receiver
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val status = extras.get(SmsRetriever.EXTRA_STATUS) as Status

                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get SMS message contents
                        var message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                        val m = p.matcher(message)
                        if (m.find()) {
                            if (otpReceiver != null) {
                                otpReceiver!!.onOTPReceived(m.group(0))
                            }

                        }


                    }
                    CommonStatusCodes.TIMEOUT -> {
                        if (otpReceiver != null) {
                            otpReceiver!!.onOTPTimeOut()
                        }
                    }
                }
            }


        }

    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String)

        fun onOTPTimeOut()
    }
}