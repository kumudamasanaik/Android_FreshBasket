package com.freshbasket.customer.screens.notification

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
import com.freshbasket.customer.model.NotificationResp
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class NotificationActivity : SubBaseActivity(), IAdapterClickListener, NotificationContract.View {
    lateinit var notificationAdapter: BaseRecAdapter
    private lateinit var snackbbar: View
    private lateinit var notificationListRes: NotificationResp

    private lateinit var presenter: NotificationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_notification, fragment_layout)
        setToolBarTittle(getString(R.string.notifications))
        hideCartView()
        init()

    }

    override fun init() {
        presenter = NotificationPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { callNotification() }
        error_button.setOnClickListener { callNotification() }
        setupNotificationRecyclerView()
        callNotification()
    }

    override fun callNotification() {

        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callNotificationApi()

        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }


    }

    override fun setNotificationResp(res: NotificationResp) {
        if (Validation.isValidStatus(res) && Validation.isValidObject(res)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            notificationListRes = res
            if (Validation.isValidList(res.result)) {
                setupData()
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)

        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)

    }

    private fun setupData() {
        if (Validation.isValidList(notificationListRes.result))
            notificationAdapter.addList(notificationListRes.result!!)

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


    private fun setupNotificationRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        notificationAdapter = BaseRecAdapter(this, R.layout.partialy_notification_list_items, adapterType = Constants.TYPE_NOTIFICATION_ADAPTER, adapterClickListener = this)
        rv_notification.apply {
            layoutManager = linearLayout
            adapter = notificationAdapter
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

}
