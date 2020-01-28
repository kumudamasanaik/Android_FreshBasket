package com.freshbasket.customer.screens.base

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.screens.addressselection.SelectAddressActivity
import com.freshbasket.customer.screens.cart.CartActivity
import com.freshbasket.customer.screens.navigation.NavigationFragment
import com.freshbasket.customer.screens.search.SearchActivity
import com.freshbasket.customer.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_sub_base.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.partialy_searchview.*
import kotlinx.android.synthetic.main.tool_bar_with_back.*

abstract class SubBaseActivity : AppCompatActivity() {
    lateinit var context: Context
    private var sharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_base)
        initScreen()

    }

    private fun initScreen() {
        context = this
        drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setSupportActionBar(my_tool_bar)
        showBack()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setToolbarScrollFlags()
        setToolBarTittle(getString(R.string.app_name))
        hideSearchView()
        hideFilterIcon()
        //setUpNavigationDrawer()

        ic_add_address.setOnClickListener {
            navigateToAddAddressScreen()
        }

        ic_cart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))

        }

        layout_search_.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    private fun navigateToAddAddressScreen() {
        val intent = Intent(context, SelectAddressActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.SUB_BASE)
        }
        startActivity(intent)
        finish()
        //startActivity(Intent(this, DeliveryAddressActivity::class.java))
    }

    private fun setToolbarScrollFlags(setFlags: Boolean = false) {
        val params: AppBarLayout.LayoutParams = my_tool_bar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = if (setFlags) AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS else 0
    }

    fun setToolBarTittle(title: String?) {
        tv_screen_title.text = title
    }

    fun hideBackbtn() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_navigation)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    fun showMenu() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_three_line)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
    }

    private fun showBack() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_navigation)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
    }

    fun showSearchView() {
        search_view!!.visibility = View.VISIBLE
    }

    fun hideSearchView() {
        search_view!!.visibility = View.GONE
    }

    fun showCartView() {
        ic_cart!!.visibility = View.VISIBLE
    }

    fun hideCartView() {
        cart_layout!!.visibility = View.GONE
    }


    fun showAddAddressView() {
        ic_add_address!!.visibility = View.VISIBLE
    }

    fun hideCAddAddressView() {
        ic_add_address!!.visibility = View.GONE
    }

    fun hideCatDropDownView() {
        cat_drop_down!!.visibility = View.GONE
    }

    fun ShowCatDropDownView() {
        cat_drop_down!!.visibility = View.VISIBLE
    }

    fun hideFilterIcon() {
        ic_filter!!.visibility = View.GONE
    }

    fun showFilterIcon() {
        ic_filter!!.visibility = View.VISIBLE
    }


    private fun setUpNavigationDrawer() {
        lateinit var newFragment: Fragment
        val ft = supportFragmentManager.beginTransaction()
        newFragment = NavigationFragment.newInstance()
        ft.replace(R.id.navigation_drawer, newFragment).commit()
        newFragment.setDrawer(drawer_layout)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer_layout!!.openDrawer(GravityCompat.START)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onResume() {
        super.onResume()

        registerSharedPreferenceChangeListener()
        updateCartCount()


    }

    private fun registerSharedPreferenceChangeListener() {
        sharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            key?.let {
                if (it.contentEquals(SharedPreferenceManager.PREF_CART_DATA)) {
                    updateCartCount()
                }
            }
        }
        SharedPreferenceManager.getPrefs().registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)

    }

    private fun updateCartCount() {
        SharedPreferenceManager.getCartData()?.apply {
            if (!cart_count.isNullOrEmpty() && cart_count!!.toInt() > 0) {
                cart_badge.text = cart_count
                cart_badge.visibility = View.VISIBLE
                // cart_badge.BlinkAnimation()
            } else
                cart_badge.visibility = View.GONE
        }


    }


    override fun onPause() {
        super.onPause()
        unregisterSharedPreferenceChangeListener()
    }

    private fun unregisterSharedPreferenceChangeListener() {
        SharedPreferenceManager.getPrefs().unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

}
