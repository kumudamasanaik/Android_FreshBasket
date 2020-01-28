package com.freshbasket.customer.screens.specialshop

import android.os.Bundle
import android.view.MenuItem
import com.freshbasket.customer.R
import com.freshbasket.customer.screens.base.SubBaseActivity
import kotlinx.android.synthetic.main.app_bar_home.*

class SpecialShopActivity : SubBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_special_shop, fragment_layout)
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
