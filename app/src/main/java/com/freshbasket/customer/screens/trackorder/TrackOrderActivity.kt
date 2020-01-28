package com.freshbasket.customer.screens.trackorder

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.HomeResp
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_track_order.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class TrackOrderActivity : SubBaseActivity(), IAdapterClickListener,TrackOrderContract.View {

    private lateinit var snackbbar: View
    private lateinit var trackOrderAdapter: BaseRecAdapter
    private lateinit var presenter: TrackOrderPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_track_order, fragment_layout)
        setToolBarTittle(getString(R.string.trackOrder))
        hideCartView()
        init()
    }

    override fun init() {
        showSearchView()
        setupTrackOrderRecyclerView()
        presenter = TrackOrderPresenter(this)
        snackbbar=snack_barview
        empty_button.setOnClickListener { callTrackOrder() }
        error_button.setOnClickListener { callTrackOrder() }
        callTrackOrder()

    }

    override fun callTrackOrder() {

        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)

            presenter.doTrackOrder()
        } else{
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)

        }

    }

    private fun setupTrackOrderRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        trackOrderAdapter = BaseRecAdapter(this, R.layout.adapter_order_list, adapterType = Constants.TYPE_TRACK_ORDER_ADAPTER, adapterClickListener = this)
        val layoutManager = linearLayout
        rv_track_order.apply {
            setLayoutManager(layoutManager)
            //addItemDecoration(InsetDecoration(context))
            adapter = trackOrderAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {

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
    override fun setTrackOrderResp(res: HomeResp) {

    }

    override fun showMsg(msg: String?) {
        showSnackBar(snackbbar,msg!!)
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
}