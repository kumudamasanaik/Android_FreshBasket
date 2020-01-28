package com.freshbasket.customer.screens.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.model.inputmodel.SignUpInput
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.landing.LandingActivity
import com.freshbasket.customer.screens.otp.OtpActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.MyLogUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showToastMsg
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.partial_social_login_layout.*

class SignUpActivity : SubBaseActivity(), SignUpContract.View, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var mContext: Context
    lateinit var presenter: SignUpPresenter
    private val TAG = "SignUpActivity"
    private lateinit var registerInput: SignUpInput
    private lateinit var source: String
    private lateinit var gender: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_register, fragment_layout)
        setToolBarTittle(getString(R.string.register))

        mContext = this
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            MyLogUtils.d(TAG, source)
        }
        hideCartView()
        init()

    }

    override fun init() {
        presenter = SignUpPresenter(this)
        setupGenderListData()
        setupCompondDrawable()
        sign_up_btn.setOnClickListener(this)
        tv_facebook.setOnClickListener(this)
        tv_google.setOnClickListener(this)
    }

    private fun setupCompondDrawable() {
        et_firstName.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_user_name), null, null, null)
        et_lastName.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_user_name), null, null, null)
        et_mobileNumber.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_mobile_no), null, null, null)
        et_emailId.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_email), null, null, null)
        et_confirmPass.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_lock), null, null, null)
        et_pass.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_lock), null, null, null)
        et_bankAccount.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_lock), null, null, null)
        et_ifscCode.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_lock), null, null, null)
    }

    private fun setupGenderListData() {
        val genderList = resources.getStringArray(R.array.gender_array)
        val adaptergender = ArrayAdapter<String>(this, R.layout.simple_spinner_item, genderList)
        adaptergender.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        sp_gender.adapter = adaptergender
        sp_gender.onItemSelectedListener = this
        linear_layout.setOnClickListener {
            sp_gender.performClick()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.sp_gender -> {
                gender = parent.getItemAtPosition(position).toString()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.sign_up_btn ->
                doRegister()

            R.id.tv_facebook ->
                navigateToLandingScreen(Constants.SOCIAL_TYPE_FACEBOOK)

            R.id.tv_google ->
                navigateToLandingScreen(Constants.SOCIAL_TYPE_GOOGLE)
        }
    }

    private fun navigateToLandingScreen(socialType: String) {
        val intent = Intent(context, LandingActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.SOURCE_SIGN)
            putExtra(Constants.SOCIALTYPE, socialType)
        }
        startActivity(intent)
        finish()
    }

    override fun doRegister() {
        registerInput = SignUpInput(
                first_name = et_firstName.text.toString(),
                last_name = et_lastName.text.toString(),
                gender = gender,
                mobile = et_mobileNumber.text.toString(),
                email = et_emailId.text.toString(),
                password = et_pass.text.toString(),
                confirm_password = et_confirmPass.text.toString(),
                pancard_pic = tv_upload_pan.text.toString(),
                aadhar_pic = tv_upload_adhar.text.toString(),
                bank_acc_number = et_bankAccount.text.toString(),
                ifsc_code = et_ifscCode.text.toString(),
                sms_key = CommonUtils.getOtpVerificationHashKey()
        )

        if (presenter.validate(registerInput)) {
            if (NetworkStatus.isOnline2(this)) {
                showLoader()
                registerInput.confirm_password = null
                presenter.callRegister(registerInput)
            } else
                Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    override fun setRegsiterRes(res: CustomerRes) {
        if (Validation.isValidStatus(res)) {
            hideLoader()
            if (Validation.isValidList(res.result)) {
                CommonUtils.setCustomerData(res.result[0])
                navigateOTPScreen()
            } else
                showMsg(res.message ?: getString(R.string.woops_something_went_wrong))
        } else {
            if (Validation.isValidString(res.error_code) && res.error_code == Constants.ERROR_CODE_100) {
                showMsg(res.message!!)
                navigateOTPScreen()
            } else
                showMsg(res.message!!)
        }
    }

    fun navigateOTPScreen() {
        val bundle = Bundle()
        bundle.putString(Constants.SOURCE, Constants.SOURCE_REGISTRATION)
        bundle.putString(Constants.EMAIL, CommonUtils.getEmailId())
        CommonUtils.startActivity(this, OtpActivity::class.java, bundle, false)
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

    override fun showSignupValidateErrorMsg(msg: String) {
        when (msg) {
            "1" -> et_firstName.error = getString(R.string.err_please_enter_valid_firstname)
            "2" -> et_lastName.error = getString(R.string.err_please_enter_valid_lastname)
            "3" -> showMsg(getString(R.string.err_please_enter_gender))
            "4" -> et_mobileNumber.error = getString(R.string.err_please_enter_valid_mobile)
            "5" -> et_emailId.error = getString(R.string.err_please_enter_valid_email)
            "6" -> et_pass.error = getString(R.string.err_please_enter_valid_pass)
            "7" -> et_confirmPass.error = getString(R.string.err_conf_pass_match)
            "8" -> et_bankAccount.error = getString(R.string.err_please_enter_valid_bank_acc_num)
            "9" -> et_ifscCode.error = getString(R.string.err_please_enter_valid_ifsc_code)
        }
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
}