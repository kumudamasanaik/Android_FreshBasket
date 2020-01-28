package com.freshbasket.customer.screens.login

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.content.res.AppCompatResources
import android.util.Base64
import android.util.Log
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.screens.landing.LandingActivity
import com.freshbasket.customer.screens.otp.OtpActivity
import com.freshbasket.customer.screens.register.SignUpActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showToastMsg
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.partial_social_login_layout.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity(), LoginContract.View, View.OnClickListener {

    private lateinit var context: Context
    private lateinit var auth: FirebaseAuth
    private val TAG = "SignInActivity"
    private lateinit var presenter: LoginPresenter
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = this
        init()
    }

    override fun init() {
        presenter = LoginPresenter(this)

        btn_login.setOnClickListener(this)
        tv_skip_login.setOnClickListener(this)
        tv_forgotPassword.setOnClickListener(this)
        tv_signUp.setOnClickListener(this)
        tv_facebook.setOnClickListener(this)
        tv_google.setOnClickListener(this)
        //printhashkey()

        et_email.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_email),
                null, null, null)
        et_password.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_lock),
                null, null, null)
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_login ->
                doLogin()

            R.id.tv_forgotPassword -> {
                email = et_email.text.toString()
                doForgotPassword()
            }


            R.id.tv_skip_login ->
                navigateHomeScreen()

            R.id.tv_signUp ->
                navigateSignupScreen()

            R.id.tv_facebook ->
                navigateToLandingScreen(Constants.SOCIAL_TYPE_FACEBOOK)

            R.id.tv_google ->
                navigateToLandingScreen(Constants.SOCIAL_TYPE_GOOGLE)

        }

    }

    private fun doForgotPassword() {
        if (presenter.validateForgotPassword(email)) {
            if (NetworkStatus.isOnline2(this)) {
                showLoader()
                presenter.forgotPassowrd(email)

            } else {
                showToastMsg(getString(R.string.error_no_internet))
            }

        }
    }

    override fun setForgotPsswrdResp(res: CustomerRes) {
        if (Validation.isValidStatus(res)) {
            hideLoader()
            if (res.result.isNotEmpty()) {
                CommonUtils.setCustomerData(res.result[0])
                showMsg(res.message)
                //showMsg(res.result[0].otp)
                navigateOtpScreen()
            } else
                showMsg(res.message ?: getString(R.string.woops_something_went_wrong))
        } else if (res.message != null) {
            showMsg(res.message!!)
        }
    }


    override fun invalidEmailPhone() {
        et_email.error = getString(R.string.error_invalid_email)

    }

    override fun invalidPass() {
        et_password.error = getString(R.string.error_invalid_password)

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


    private fun navigateToLandingScreen(socialType: String) {

        if (NetworkStatus.isOnline2(this)) {
            val intent = Intent(context, LandingActivity::class.java)
            intent.apply {
                putExtra(Constants.SOURCE, Constants.SOURCE_SIGN)
                putExtra(Constants.SOCIALTYPE, socialType)
            }
            startActivity(intent)
            finish()
        } else
            showToastMsg(getString(R.string.error_no_internet))


    }


    override fun doLogin() {
        if (presenter.validateLogin(et_email.text.toString(), et_password.text.toString())) {
            CommonUtils.setCustomerPassword(et_password.text.toString())
            if (NetworkStatus.isOnline2(this)) {
                showLoader()
                presenter.doLogin(et_email.text.toString(), et_password.text.toString())
            } else {
                showToastMsg(getString(R.string.error_no_internet))
            }
        }
    }

    override fun setLoginResp(res: CustomerRes) {
        if (Validation.isValidStatus(res)) {
            hideLoader()
            if (res.result.isNotEmpty()) {
                CommonUtils.setCustomerData(res.result[0])
                navigateHomeScreen()
            } else
                showMsg(res.message ?: getString(R.string.woops_something_went_wrong))
        } else if (res.message != null) {
            showMsg(res.message!!)
        }
    }


    fun navigateHomeScreen() {
        //startActivity(Intent(this, HomeActivity::class.java))
        val intent = Intent(context, HomeActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.LOGIN_SCREEN)
        }
        startActivity(intent)
        finish()
    }


    fun navigateSignupScreen() {
        val bundle = Bundle()
        bundle.putString(Constants.SOURCE, Constants.SOURCE_REGISTRATION)
        CommonUtils.startActivity(this, SignUpActivity::class.java, bundle, false)
    }


    fun navigateOtpScreen() {
        val bundle = Bundle()
        bundle.putString(Constants.SOURCE, Constants.SOURCE_LOGIN)
        bundle.putString(Constants.EMAIL, email)
        CommonUtils.startActivity(this, OtpActivity::class.java, bundle, false)
    }

    public override fun onStart() {
        super.onStart()

    }


    private fun printhashkey() {
        try {
            val info = packageManager.getPackageInfo(
                    "com.freshbasket.customer",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("Exception:", "PackageManager.NameNotFoundException")
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Exception:", "NoSuchAlgorithmException")
        }
    }

}



