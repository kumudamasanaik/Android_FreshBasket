package com.freshbasket.customer.screens.cartsummary

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.CartSummaryResp
import com.freshbasket.customer.model.CheckOutResp
import com.freshbasket.customer.model.PayOption
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.screens.coupon.CouponActivity
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.util.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_cart_summary.*
import kotlinx.android.synthetic.main.activity_payment.view.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.partial_delivery_time_slot_dialog_header.view.*
import kotlinx.android.synthetic.main.partial_delivery_time_slot_dialog_parent_view.view.*
import kotlinx.android.synthetic.main.partial_summary_view.*

class CartSummaryActivity : SubBaseActivity(), CartSummaryContract.View, IAdapterClickListener, PaymentResultListener {


    private lateinit var cartSummaryProductsAdapter: BaseRecAdapter
    private val TAG = "CartSummaryActivity"
    private lateinit var mContext: Context
    private var cartSummaryResp: CartSummaryResp? = null
    private lateinit var presenter: CartSummaryPresenter
    private var paymentType: String? = null
    private lateinit var activity: Activity
    private var orderId: Int = 0
    private var discount: String = ""
    private var grant_tot: Float = 0.0f
    private var grant_tot_amt: Float = 0.0f
    private var jsonArrayInput: JsonArray? = null
    private var checkoutResp: CheckOutResp? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_cart_summary, fragment_layout)
        setToolBarTittle(getString(R.string.cart_summary))

        if (intent != null) {
            discount = intent.getStringExtra(Constants.PROMO_CODE_AMT) ?: ""
        }

        init()
    }

    override fun init() {
        mContext = this
        activity = this
        presenter = CartSummaryPresenter(this)
        Checkout.preload(applicationContext)
        tv_terms_conditions.paintFlags = tv_terms_conditions.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        setupCartSummaryProducts()
        hideCartView()
        callCartSummaryApi()

        if (discount.isNotEmpty()) {
            ll_promo_applied.visibility = View.VISIBLE
            val amt = "-" + CommonUtils.getRupeesSymbol(mContext, discount)
            tv_promo_price.text = amt
        }

        tv_payment.setOnClickListener {
            if (tv_timeslot.text == getString(R.string.dummy_time_slot))
                toast("Please select a time slot")
            else
                showpaymentmethodDialog()
        }

        view_timeslot.setOnClickListener {
            showTimeSlotDialog()
        }

        view_product_orders.setOnClickListener {
            if (rv__order_detail_delivery.visibility == View.VISIBLE)
                rv__order_detail_delivery.visibility = View.GONE
            else
                rv__order_detail_delivery.visibility = View.VISIBLE
        }

        empty_button.setOnClickListener { callCartSummaryApi() }
        error_button.setOnClickListener { callCartSummaryApi() }

        tv_timeslot_header.setCompoundDrawablesWithIntrinsicBounds(null,
                null, AppCompatResources.getDrawable(this, R.drawable.ic_edit), null)

        ll_promo_apply.setOnClickListener {
            startActivity(Intent(this, CouponActivity::class.java).putExtra(Constants.ORDER_ID, orderId))
        }
    }

    private fun showTimeSlotDialog() {
        val builder = Dialog(this, R.style.CustomDialogThemeLightBg)
        builder.setCanceledOnTouchOutside(true)
        val inflater = LayoutInflater.from(context)
        val dialogview = inflater.inflate(R.layout.partial_delivery_time_slot_dialog_parent_view, null)
        builder.setContentView(dialogview)
        builder.show()
        dialogview.layout_delivery_slot.removeAllViews()
        cartSummaryResp?.apply {
            if (Validation.isValidList(orders) && Validation.isValidList(orders!![0].deliveryslot)) {
                for (Curheader in orders!![0].deliveryslot!!) {
                    val infalt = LayoutInflater.from(context)
                    val dialogheader: View = infalt.inflate(R.layout.partial_delivery_time_slot_dialog_header, null)
                    if (!Curheader.date.isNullOrEmpty()) {
                        dialogheader.delivery_slot_name.text = Curheader.date
                        dialogheader.delivery_slot_name.setBackgroundResource(R.color.color_yellow)
                    }

                    dialogview.layout_delivery_slot.addView(dialogheader)
                    if (Validation.isValidList(Curheader.times)) {
                        for (childSlot in Curheader.times!!) {
                            val chldInf = LayoutInflater.from(context)
                            val childlayout: View = chldInf.inflate(R.layout.partial_delivery_time_slot_dialog_header, null)
                            if (!childSlot.time.isNullOrEmpty())
                                childlayout.delivery_slot_name.text = childSlot.time


                            childlayout.delivery_slot_name.setOnClickListener {
                                builder.dismiss()
                                setDeliverySlotTime(Curheader.date, childSlot.time)

                            }

                            dialogview.layout_delivery_slot.addView(childlayout)
                        }
                    }
                }

            }

        }

    }

    private fun setDeliverySlotTime(deliveryDate: String?, deliveryTime: String?) {

        if (Validation.isValidString(deliveryTime) && Validation.isValidString(deliveryDate)) {
            tv_timeslot.text = " - " + deliveryDate.plus(" ").plus(deliveryTime)
            CommonUtils.setSelectedTimeslot(deliveryDate.plus("," + deliveryTime))
        }

    }

    override fun callCartSummaryApi() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callCartSummaryApi()

        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setupCheckoutApi() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callCheckOutApi(jsonArrayInput!!)
        } else {
            showMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setCartSummaryApiResp(cartsummaryres: CartSummaryResp) {
        if (Validation.isValidObject(cartsummaryres)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            cartSummaryResp = cartsummaryres
            orderId = cartsummaryres.orders!![0].order_id!!
            setData()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)

    }

    override fun setcheckoutApiResp(checkoutResp: CheckOutResp) {
        if (Validation.isValidStatus(checkoutResp)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            this.checkoutResp = checkoutResp
            updateCheckOutApiStatus()

        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }


    private fun setData() {
        CommonUtils.saveCartSummaryResp(cartSummaryResp!!)
        cartSummaryResp?.apply {

            summary?.apply {
                tv_total_mrp.text = CommonUtils.getRupeesSymbol(mContext, selling_price)
                tv_total_savings.text = CommonUtils.getRupeesSymbol(mContext, delivery_charge)
                tv_grand_total.text = CommonUtils.getRupeesSymbol(mContext, grand_total)
                grant_tot = grand_total.toFloat()
                tv_payment.text = getString(R.string.payment).plus("  " + CommonUtils.getRupeesSymbol(mContext, grand_total))

                if (discount.isNotEmpty()) {
                    grant_tot_amt = grant_tot.minus(discount.toFloat())
                    tv_grand_total.text = CommonUtils.getRupeesSymbol(mContext, grant_tot_amt.toString())
                    tv_payment.text = getString(R.string.payment).plus("  " + CommonUtils.getRupeesSymbol(mContext, grant_tot_amt.toString()))

                }
            }

            address?.apply {
                delivery_address.text = getAddressString()
            }

            if (Validation.isValidList(cart))
                cartSummaryProductsAdapter.addList(cart!!)

            /*  if (Validation.isValidList(orders))
                  setDeliverySlotTime(orders!![0].deliveryslot!![0].date, orders!![0].deliveryslot!![0].times!![0].time)*/


        }


    }

    private fun showpaymentmethodDialog() {
        val factory = LayoutInflater.from(context)
        val dialogView = factory.inflate(R.layout.activity_payment, null)

        val dialogAdapter = BaseRecAdapter(context, R.layout.partial_layout_cash_on_delivery, adapterClickListener = this)
        dialogView.rv_cash_on_del.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        dialogView.rv_cash_on_del.adapter = dialogAdapter

        cartSummaryResp?.apply {
            if (Validation.isValidList(pay_options))
                dialogAdapter.addList(cartSummaryResp!!.pay_options!!)
        }
        val dialog = AlertDialog.Builder(context).create()
        dialog.setView(dialogView)

        dialog.show()

    }

    private fun setupCheckoutpiInputs() {
        jsonArrayInput = JsonArray()
        cartSummaryResp?.apply {

            if (Validation.isValidList(orders)) {
                for (curOrder in orders!!) {

                    val jsonInput = JsonObject()
                    jsonInput.addProperty(Constants.ID_ORDER, curOrder.order_id)
                    jsonInput.addProperty(Constants.TOTAL_PAID, curOrder.grand_total)
                    jsonInput.addProperty(Constants.PAY_TYPE, paymentType)
                    jsonInput.addProperty(Constants.DELIVERY_SLOT, CommonUtils.getDeliverySelectedTimeSlot())
                    jsonInput.addProperty(Constants.PAYMENT_EXPRESS, CommonUtils.getDeliverySelectedMethod())
                    jsonInput.addProperty(Constants.WALLET_AMT, "")
                    jsonArrayInput!!.add(jsonInput)
                }
            }


        }

    }


    private fun setupCartSummaryProducts() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        cartSummaryProductsAdapter = BaseRecAdapter(context = this, type = R.layout.adapter_order_list, adapterClickListener = this, adapterType = Constants.TYPE_CART_SUMMARY)
        rv__order_detail_delivery.apply {
            layoutManager = linearLayout
            adapter = cartSummaryProductsAdapter
            isNestedScrollingEnabled = false
        }
    }


    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (type == Constants.PAY_TYPE) {
            val paymentMethod = item as PayOption
            if (Validation.isValidObject(paymentMethod)) {
                paymentMethod.apply {
                    paymentType = paymentMethod.type
                    setupCheckoutpiInputs()
                    setupPaymentMethod()
                }
            }
        }

    }

    private fun setupPaymentMethod() {
        paymentType?.apply {
            if (paymentType!!.contentEquals(Constants.PAYMENT_TPE_ONLINE))
                setupRazerPayPayment()

            if (paymentType!!.contentEquals(Constants.PAYMENT_TPE_COD))
                setupCheckoutApi()

        }
    }


    private fun updateCheckOutApiStatus() {
        CommonUtils.clearSharedPrefernceData()
        checkoutResp?.apply {

            SharedPreferenceManager.saveCartData(null)
            CommonUtils.showCustomToast(mContext, checkoutResp!!.message!!, 1)
            navigateToHomeActivtyScreen()
        }
    }

    private fun setupRazerPayPayment() {
        val checkout = Checkout()
        cartSummaryResp?.apply {
            if (Validation.isValidObject(cartSummaryResp!!.summary)) {
                try {
                    checkout.setImage(R.drawable.keep_cart_logo)
                    checkout.setFullScreenDisable(false)
                    val jsonObject = CommonUtils.getPaymentJsonParameter(cartSummaryResp!!)
                    checkout.open(activity, jsonObject)
                } catch (e: Exception) {
                    MyLogUtils.d(TAG, e.printStackTrace().toString())
                }

            }
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

    override fun onPaymentError(p0: Int, msg: String?) {
        CommonUtils.showCustomToast(mContext, msg!!, 1)

    }

    override fun onPaymentSuccess(msg: String?) {
        setupCheckoutApi()
    }

    fun navigateToHomeActivtyScreen() {
        val bundle = Bundle()
        bundle.putString(Constants.SOURCE, Constants.SOURCE_CART_SUMMARY)
        bundle.putString(Constants.PAYMENT_TPE, paymentType)
        CommonUtils.startActivity(this, HomeActivity::class.java, bundle, false)
        finish()
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