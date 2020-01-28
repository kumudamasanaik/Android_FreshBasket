package com.freshbasket.customer.screens.otp

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.broadcastreceiver.MySMSBroadcastReceiver
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.Customer
import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.changepassword.ChangePasswordActivity
import com.freshbasket.customer.screens.forgotpassword.ResetPasswordActivity
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.MyLogUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showToastMsg
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.app_bar_home.*

class OtpActivity : SubBaseActivity(), OtpContract.View, View.OnClickListener, MySMSBroadcastReceiver.OTPReceiveListener {


    private lateinit var otp: String
    lateinit var presenter: OtpPresenter
    private lateinit var source: String
    private lateinit var mContext: Context
    private val TAG = "OtpActivity"
    private lateinit var email: String
    private lateinit var mySMSBroadcastReceiver: MySMSBroadcastReceiver
    private lateinit var intentFilter: IntentFilter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_otp, fragment_layout)
        setToolBarTittle(getString(R.string.verifyOtp))

        mContext = this

        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            email = intent.getStringExtra(Constants.EMAIL)
            MyLogUtils.d(TAG, source)
        }
        init()
    }

    override fun init() {
        hideCartView()
        tv_ver_otp.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_right_arrow), null)

        presenter = OtpPresenter(this)
        // dummyUpdateOTP()
        setupOTpReader()

        tv_ver_otp.setOnClickListener(this)
        tv_resend.setOnClickListener(this)
    }

    private fun setupOTpReader() {
        mySMSBroadcastReceiver = MySMSBroadcastReceiver()

        val client = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()
        mySMSBroadcastReceiver.initOTPListener(this)
        intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(mySMSBroadcastReceiver, intentFilter)


    }

    private fun dummyUpdateOTP() {

        if (CommonUtils.getOTP().isNotEmpty())
            tv_enter_otp.setText(CommonUtils.getOTP())

    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.tv_ver_otp ->
                doVerifyOtp()
            R.id.tv_resend ->
                doResendOtp()
        }
    }

    fun navigateToChangePsswrdScreen() {
        val bundle = Bundle()
        bundle.putString(Constants.SOURCE, Constants.SOURCE_OTP)
        CommonUtils.startActivity(this, ResetPasswordActivity::class.java, bundle, false)
    }

    override fun doVerifyOtp() {
        if (!(Validation.isValidOtp(tv_enter_otp.text.toString()))) {
            Toast.makeText(this@OtpActivity, getString(R.string.enter_valid_otp), Toast.LENGTH_SHORT).show()
        } else {
            if (NetworkStatus.isOnline2(this)) {
                showLoader()
                presenter.verifyOtp(tv_enter_otp.text.toString(), email)
            }
        }
    }

    override fun setVerifyOtpRes(res: CustomerRes) {
        if (Validation.isValidStatus(res)) {
            hideLoader()
            tv_enter_otp.setText(CommonUtils.getOTP())
            showMsg(res.message)
            setupData(res.result[0])
        } else {
            showMsg(res.message ?: getString(R.string.Whoops_Something_went_wrong))
        }
    }

    private fun setupData(customer: Customer) {
        CommonUtils.setCustomerData(customer)
        if (source.isNotEmpty() && source.contentEquals(Constants.SOURCE_LOGIN))
            navigateToChangePsswrdScreen()
        else
            navigateToHomeScreen()

    }

    private fun doResendOtp() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.resendOtp(email)
        } else
            showMsg(getString(R.string.error_no_internet))
    }

    override fun setResendOtpRes(res: CommonRes) {
        if (Validation.isValidStatus(res) && Validation.isValidString(res.code)) {
            showMsg(res.message)
            if (!res.code.isNullOrEmpty())
                tv_enter_otp.setText(res.code)

        } else
            showMsg(res.message)
    }

    private fun navigateToHomeScreen() {
        //startActivity(Intent(this, HomeActivity::class.java))
        val intent = Intent(context, HomeActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.OTP_SCREEN)
        }
        startActivity(intent)
        finish()
    }

    override fun showMsg(msg: String?) {
        showToastMsg(msg!!)
    }

    override fun showLoader() {
        CommonUtils.showLoading(this, true)
    }

    override fun hideLoader() {
        CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {
    }


    override fun onOTPReceived(otp: String) {
        if (otp.length > 0) {
            tv_enter_otp.setText(CommonUtils.getOTP())
            doVerifyOtp()

        }

    }

    override fun onOTPTimeOut() {
        showMsg(getString(R.string.Whoops_Something_went_wrong))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mySMSBroadcastReceiver != null)
            unregisterReceiver(mySMSBroadcastReceiver)
    }
}