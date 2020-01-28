package com.freshbasket.customer.screens.customerservices

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_customer_services.*
import kotlinx.android.synthetic.main.app_bar_home.*

class CustomerServicesActivity : SubBaseActivity() {


    private val TAG = "CustomerServiceActivity"
    private var mContext: Context? = null
    private lateinit var snackbbar: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_customer_services, fragment_layout)
        setToolBarTittle(getString(R.string.customerServices))
        mContext = this@CustomerServicesActivity


        init()
        hideCartView()

    }

    private fun init() {
        snackbbar = snack_barview
        updateCustomerCareData()

        tv_email.setOnClickListener {
            if (tv_email.text.isNotEmpty())
                CommonUtils.goToEmailApp(context, tv_mobile.text.toString().trim { it <= ' ' }, "KeepCart Android App")
        }
        tv_mobile.setOnClickListener {
            if (tv_mobile.text.isNotEmpty()) {
                CommonUtils.goToDialPad(context, tv_mobile.text.toString().trim { it <= ' ' })
            }
        }

    }

    private fun updateCustomerCareData() {
        if (NetworkStatus.isOnline2(this)) {
            if (Validation.isValidString(CommonUtils.getCustomerEmail()))
                tv_email.text = CommonUtils.getCustomerEmail()
            if (Validation.isValidString(CommonUtils.getCustomerCareMobileNumber()))
                tv_mobile.text = CommonUtils.getCustomerCareMobileNumber()

        } else
            showMsg(getString(R.string.error_no_internet))

    }

    fun showViewState(state: Int) {
        if (base_multistateview != null)
            base_multistateview.viewState = state
    }

    fun showMsg(msg: String?) {
        showSnackBar(snackbbar, msg!!)
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
