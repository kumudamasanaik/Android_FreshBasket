package com.freshbasket.customer.screens.paymentstatus

import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.MenuItem
import com.freshbasket.customer.R
import com.freshbasket.customer.screens.base.SubBaseActivity
import kotlinx.android.synthetic.main.activity_payment_status.*
import kotlinx.android.synthetic.main.app_bar_home.*

class PaymentStatusActivity : SubBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_payment_status, fragment_layout)
        setToolBarTittle(getString(R.string.paymentStatus))
        initScreens()

    }

    private fun initScreens() {
        payment_ok.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_right_arrow), null)

        hideCartView()
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
