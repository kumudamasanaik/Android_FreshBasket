package com.freshbasket.customer.screens.mywallet

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class MyWalletActivity : SubBaseActivity(), IAdapterClickListener, MyWalletContract.View {
    private lateinit var snackbbar: View
    private lateinit var presenter: MyWalletPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_my_wallet, fragment_layout)
        setToolBarTittle(getString(R.string.myWallet))
        hideCartView()
        init()
    }

    override fun init() {
        presenter = MyWalletPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { callMyWalletApi() }
        error_button.setOnClickListener { callMyWalletApi() }
        // callMyWalletApi()

    }

    override fun callMyWalletApi() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)

            presenter.callMyWalletApi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)

        }
    }

    override fun setMyWalletApiResp(homeResp: HomeResp) {
      /*  if (Validation.isValidStatus(res) && Validation.isValidObject(res)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            orderListResp = res
            if (res.orders.isNotEmpty()) {

            setData()
        } else
            showViewState(MultiStateView.VIEW_STATE_EMPTY)
    } else
        showViewState(MultiStateView.VIEW_STATE_ERROR)*/
    }





    private fun setData() {

    }


    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
    }


    override fun showMsg(msg: String?) {
        showSnackBar(snackbbar, msg!!)
    }

    override fun showLoader() {
        CommonUtils.showLoading(this, true)
    }

    override fun hideLoader() {
        CommonUtils.hideLoading()

    }

    override fun showViewState(state: Int) {
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
