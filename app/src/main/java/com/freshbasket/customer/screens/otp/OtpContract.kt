package com.freshbasket.customer.screens.otp

import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView

interface OtpContract {
    interface View : BaseView {
        fun doVerifyOtp()
        fun setVerifyOtpRes(res: CustomerRes)
        fun setResendOtpRes(res: CommonRes)

    }

    interface Presenter : BasePresenter<View> {
        fun verifyOtp(otp: String,email_id: String)
        fun resendOtp(email_id: String)

    }
}