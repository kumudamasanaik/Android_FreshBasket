package com.freshbasket.customer.screens.forgotpassword

import android.content.Context
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.MenuItem
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.changepassword.ChangePasswordContract
import com.freshbasket.customer.screens.changepassword.ChangePasswordPresenter
import com.freshbasket.customer.screens.login.LoginActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.MyLogUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showToastMsg
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.app_bar_home.*

class ResetPasswordActivity : SubBaseActivity(), ChangePasswordContract.View, View.OnClickListener {
    private val TAG = "ChangePasswordActivity"
    private lateinit var mContext: Context
    private lateinit var source: String
    private lateinit var presenter: ChangePasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_reset_password, fragment_layout)
        setToolBarTittle(getString(R.string.changePassword))
        mContext = this



        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            MyLogUtils.d(TAG, source)
        }
        hideCartView()
        init()
    }

    override fun init() {
        tv_change_password.setCompoundDrawablesWithIntrinsicBounds(null, null, (AppCompatResources.getDrawable(this, R.drawable.ic_right_arrow)), null)
        presenter = ChangePasswordPresenter(this)
        tv_change_password.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.tv_change_password ->
                doPassword()

        }
    }

    private fun doPassword(): Boolean {
        if (et_newPass.text.isNullOrEmpty() || et_reenterPass.text.isNullOrEmpty())
            return false

        if (!(et_newPass.text.toString().contentEquals(et_reenterPass.text.toString())))
            return false

        return true
    }

    override fun doChangePassword() {
        if (NetworkStatus.isOnline2(this)) {
            showLoader()
            presenter.doChangePassword(et_newPass.text.toString())
        } else
            showMsg(getString(R.string.error_no_internet))
    }

    override fun setChangepasswordRes(res: CustomerRes) {
        if (Validation.isValidStatus(res)) {
            showMsg(res.message ?: getString(R.string.passwrd_changed_successfully))
            navigateLoginScreen()
        } else
            showMsg(res.message ?: getString(R.string.Whoops_Something_went_wrong))
    }

    private fun navigateLoginScreen() {
        val bundle = Bundle()
        bundle.putString(Constants.SOURCE, Constants.SOURCE_CHANGE_PSSWRD)
        CommonUtils.startActivity(this, LoginActivity::class.java, bundle, true)
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