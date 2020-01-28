package com.freshbasket.customer.screens.changepassword

import android.content.Context
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.login.LoginActivity
import com.freshbasket.customer.util.*
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.app_bar_home.*

class ChangePasswordActivity : SubBaseActivity(), ChangePasswordContract.View, View.OnClickListener {
    private val TAG = "ChangePasswordActivity"
    private lateinit var mContext: Context
    private lateinit var source: String
    private lateinit var presenter: ChangePasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_change_password, fragment_layout)
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
                callPresenter()
        }
    }

    private fun callPresenter() {
        if (et_newPass.text.isNotEmpty() && et_reenterPass.text.isNotEmpty()) {
            if((et_newPass.text.length >= 6 && et_reenterPass.text.length >= 6 )) {
                if (et_oldPass.text.toString().contentEquals(CommonUtils.getCustomerPassword())) {
                    if (et_newPass.text.toString() == et_reenterPass.text.toString()) {
                        doChangePassword()
                    } else
                        toast(getString(R.string.err_conf_pass_match), Toast.LENGTH_LONG)
                } else
                    toast(getString(R.string.err_old_pass), Toast.LENGTH_LONG)
            }else
                toast(getString(R.string.err_please_enter_valid_pass), Toast.LENGTH_LONG)
        } else
            toast(getString(R.string.error_empty_password), Toast.LENGTH_LONG)
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