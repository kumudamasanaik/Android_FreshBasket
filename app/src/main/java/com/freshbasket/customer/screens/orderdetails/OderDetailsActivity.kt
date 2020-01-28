package com.freshbasket.customer.screens.orderdetails

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.listner.ISelectedDateListener
import com.freshbasket.customer.model.Cart
import com.freshbasket.customer.model.CommonRes
import com.freshbasket.customer.model.Order
import com.freshbasket.customer.model.OrderSummary
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_oder_details.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class OderDetailsActivity : SubBaseActivity(), IAdapterClickListener, ISelectedDateListener, OrderDetailsContract.View {

    private lateinit var snackbbar: View
    lateinit var orderdetailAdapter: BaseRecAdapter
    private lateinit var presenter: OrderDetailsPresenter
    private lateinit var order_id: String
    private lateinit var order_status: String
    private lateinit var source: String

   // private lateinit var cartList: Order
    private lateinit var Cur_order: Order
    private lateinit var cart: Cart
    private var jsonArrayInput: JsonArray? = null
    private var reason: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_oder_details, fragment_layout)
        setToolBarTittle(getString(R.string.trackOrder))
        hideCartView()
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            order_id = intent.getStringExtra(Constants.ID_ORDER_NUM)
        }
        init()
    }

    override fun init() {
        presenter = OrderDetailsPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { callOrderDetails() }
        error_button.setOnClickListener { callOrderDetails() }
        callOrderDetails()

        if (source == Constants.TRACK_ORDER) {
            rl_status.visibility = View.VISIBLE

        }
    }

    override fun callOrderDetails() {

        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callMyOrderDetailsApi(order_id)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)

        }
    }

    override fun setOrderDetailsResp(res: Order) {
        if (Validation.isValidObject(res)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            Cur_order = res
            setupOrderDetailRecyclerView()
            setOrderData()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setOrderData() {

        if (!Cur_order.order.order_status.isNullOrEmpty())
            tv_order_status.text = Cur_order.order.order_status

        if (!Cur_order.order.order_no.isNullOrEmpty())
            tv_ordersum.text = Cur_order.order.order_no


        if (!Cur_order.order.added.isNullOrEmpty())
            tv_place_on.text = Cur_order.order.added

        if (Validation.isValidObject(Cur_order.order.delivered_on)) {
            ll_delivered_on.visibility = View.VISIBLE
            tv_delivered_on.text = Cur_order.order.delivered_on.toString()
        } else
            ll_delivered_on.visibility = View.GONE


        if (!Cur_order.address.area.isNullOrEmpty())
            tv_add_details.text = Cur_order.address.area

        if (!Cur_order.order.total_mrp.isNullOrEmpty())
            tv_mrp_price.text = CommonUtils.getRupeesSymbol(context, Cur_order.order.grand_total!!)

        if (!Cur_order.order.pay_type.isNullOrEmpty())
            tv_payment_mode.text = Cur_order.order.pay_type

    }


    private fun setupOrderDetailRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            orderdetailAdapter = BaseRecAdapter(this, R.layout.adapter_order_list, adapterType = Constants.TYPE_ORDER_DETAIL_ADAPTER, adapterClickListener = this)
        rv__order_detail_delivery.apply {
            layoutManager = linearLayout
//            addItemDecoration(InsetDecoration(context))
            adapter = orderdetailAdapter
            isNestedScrollingEnabled = false
        }
        if (Validation.isValidList(Cur_order.cart))
            orderdetailAdapter.addList(Cur_order.cart!!)
    }


    private fun callReturnApi(order: OrderSummary) {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callReturnApi(id = CommonUtils.getCustomerID(), order_id = order._id!!, jsonArray = jsonArrayInput!!, return_reason = reason!!)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setReturnApiRes(res: CommonRes) {
        if (Validation.isValidStatus(res)) {
            callOrderDetails()
        } else
            showViewState(MultiStateView.VIEW_STATE_EMPTY)
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (Validation.isValidObject(item)) {
            when (op) {
                Constants.TYPE_ORDER_DETAIL_ADAPTER -> {
                    navigateTrackOrderDetail()
                }
                Constants.ORDER_RETURNED -> {
                    CommonUtils.showRetunConfirmationDialogPopup(this, this, getString(R.string.do_you_want_to_return_order), item, pos)
                }
            }
        }
    }

    override fun setSelectedDate(date: String) {
        if (date.isNotEmpty() && Validation.isValidObject(Cur_order.order)) {
            this.reason = date
            getReturnOrderInputs()
            callReturnApi(Cur_order.order)
        } else
            showMsg("please enter valid reason")

    }

    private fun getReturnOrderInputs() {
        jsonArrayInput = JsonArray()
        Cur_order.apply {
            if (Validation.isValidList(cart)) {
                for (curOrder in cart!!) {
                    val jsonInput = JsonObject()
                    jsonInput.addProperty(Constants.ID_SKU, curOrder.id_sku)
                    jsonInput.addProperty(Constants.ID_PRODUCT, curOrder.id_product)
                    jsonInput.addProperty(Constants.RETURNED_QUANTITY, curOrder.returned_quantity)
                    jsonArrayInput!!.add(jsonInput)
                }
            }
        }
    }

    private fun navigateTrackOrderDetail() {
        // startActivity(Intent(this, TrackOrderActivity::class.java))
        Toast.makeText(this, getString(R.string.need_to_implement), Toast.LENGTH_SHORT).show()

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
}
