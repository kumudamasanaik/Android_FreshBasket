package com.freshbasket.customer.screens.myorder

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
import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.MyOrderResp
import com.freshbasket.customer.model.Order
import com.freshbasket.customer.model.OrderSummary
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.screens.orderdetails.OderDetailsActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_my_order.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*


class MyOrderActivity : SubBaseActivity(), IAdapterClickListener, MyOrderContract.View {

    private lateinit var snackbbar: View
    private lateinit var myOrderAdapter: BaseRecAdapter
    private lateinit var presenter: MyOrderPresenter
    private lateinit var orderListResp: MyOrderResp
    private lateinit var order_id: String
    private lateinit var order_status: String
    private lateinit var source: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_my_order, fragment_layout)
        setToolBarTittle(getString(R.string.myOrders))
        hideCartView()
        init()
    }

    override fun init() {
        presenter = MyOrderPresenter(this)
        snackbbar = snack_barview
        setupMyOrderRecyclerView()
        empty_button.setOnClickListener { callMyOrder() }
        error_button.setOnClickListener { callMyOrder() }
        callMyOrder()

    }

    private fun setupMyOrderRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        myOrderAdapter = BaseRecAdapter(this, R.layout.partialy_my_order_list_items, adapterType = Constants.TYPE_MY_ORDER_ADAPTER, adapterClickListener = this)
        rv_orders.apply {
            layoutManager = linearLayout
            adapter = myOrderAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun callMyOrder() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callMyOrderApi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    private fun callCancelApi(item: OrderSummary) {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callCancelApi(CommonUtils.getCustomerID(), order_no = item.order_no!!)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }


    override fun setMyOrderResp(res: MyOrderResp) {
        if (Validation.isValidStatus(res) && Validation.isValidObject(res)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            orderListResp = res
            if (res.orders.isNotEmpty()) {
                setData()
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)

    }

    override fun setCancelApiRes(res: CommonRes) {
        if (Validation.isValidStatus(res)) {
            callMyOrder()
        } else
            showViewState(MultiStateView.VIEW_STATE_EMPTY)
    }

    private fun setData() {
        if (Validation.isValidList(orderListResp.orders))
            myOrderAdapter.addList(orderListResp.orders)
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {

        if (Validation.isValidObject(item) && item is Order) {
            when (op) {
                Constants.ORDER_CANCELLED -> {
                    CommonUtils.showConfirmationDialogPopup(this, this, getString(R.string.do_you_want_to_cancel_order), item, pos)
                }
                Constants.ORDER_CANCELLED_CONFIRMATION -> {
                    callCancelApi(item.order)
                }

                Constants.TYPE_MY_ORDER_LIST -> {
                    order_id = item.order.order_no!!
                    source=Constants.TYPE_MY_ORDER_LIST
                    navigateMyOrderDetail()
                }

                Constants.TRACK_ORDER ->{
                    order_id = item.order.order_no!!
                    source=Constants.TRACK_ORDER
                    navigateMyOrderDetail()
                }


            }
        }
    }


    private fun navigateMyOrderDetail() {
        var bundle = Bundle()
        bundle.putString(Constants.ID_ORDER_NUM, order_id)
        bundle.putString(Constants.SOURCE,source)
        CommonUtils.startActivity(this, OderDetailsActivity::class.java, bundle, false)
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