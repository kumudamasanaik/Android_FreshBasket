package com.freshbasket.customer.screens.referearn

import android.content.Context
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.MenuItem
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_refer_earn.*
import kotlinx.android.synthetic.main.app_bar_home.*

class ReferEarnActivity : SubBaseActivity() {
    private lateinit var snackbbar: View
    private lateinit var share_message: String
    private val TAG = "ReferEarnActivity"


    private var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_refer_earn, fragment_layout)
        setToolBarTittle(getString(R.string.referEarn))
        hideCartView()
        init()
        updateReferUI()

    }

    fun init() {
        tv_ver_otp.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_share_blue), null)
        mContext = this@ReferEarnActivity

        tv_ver_otp.setOnClickListener {


            share_message = tv_share_msg.text.toString()
            CommonUtils.shareApplication(this, share_message)


        }
    }

    private fun updateReferUI() {
        if (Validation.isValidString(CommonUtils.getReferralCode()))
            tv_referral_code.text = CommonUtils.getReferralCode()

        if (Validation.isValidString(CommonUtils.getSharedMSg()))
            tv_share_msg.text = CommonUtils.getSharedMSg()


    }


    fun showMsg(msg: String?) {
        showSnackBar(snackbbar, msg!!)
    }

    fun showLoader() {
        CommonUtils.showLoading(this, true)

    }

    fun hideLoader() {
        CommonUtils.hideLoading()

    }

    fun showViewState(state: Int) {
        if (base_multistateview != null)
            base_multistateview.viewState = state
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
