package com.freshbasket.customer.screens.payment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
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
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.util.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class PaymentActivity : SubBaseActivity(), PaymentContract.View, PaymentResultListener, IAdapterClickListener {

    private val TAG = "PaymentActivity"
    private lateinit var source: String
    private lateinit var mContext: Context
    private var paymentType: String? = null
    private lateinit var activity: Activity
    private lateinit var presenter: PaymentPresenter
    private var cartSummaryResp: CartSummaryResp? = null
    private var jsonArrayInput: JsonArray? = null
    private var checkoutResp: CheckOutResp? = null
    private lateinit var paymentOptionAdapter: BaseRecAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_payment, fragment_layout)
        mContext = this
        setToolBarTittle(getString(R.string.payment))
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            MyLogUtils.d(TAG, source)
        }

        init()
        hideCartView()
        hideSoftKeyboard()
    }

    override fun init() {
        activity = this
        presenter = PaymentPresenter(this)
        cartSummaryResp = CommonUtils.getCartSummaryResp()
        Checkout.preload(applicationContext)
        setupPaymentMethodRecylerView()
        empty_button.setOnClickListener { setupCheckoutApi() }
        error_button.setOnClickListener { setupCheckoutApi() }


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

    private fun setupPaymentMethodRecylerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        paymentOptionAdapter = BaseRecAdapter(this, R.layout.partial_layout_cash_on_delivery, adapterType = Constants.TYPE_CART_SUMMARY_ADAPTER, adapterClickListener = this)
        val layoutManager = linearLayout
        rv_cash_on_del.apply {
            setLayoutManager(layoutManager)
            adapter = paymentOptionAdapter
            isNestedScrollingEnabled = false
        }

        cartSummaryResp?.apply {
            if (Validation.isValidList(pay_options))
                paymentOptionAdapter.addList(cartSummaryResp!!.pay_options!!)
        }
    }


    override fun showMsg(msg: String?) {
        showSnackBar(parent_view, msg!!)
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

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        val paymentMethod = item as PayOption
        if (Validation.isValidObject(paymentMethod)) {
            paymentMethod.apply {
                paymentType = paymentMethod.type
                setupCheckoutpiInputs()
                setupPaymentMethod()
            }
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

    override fun setcheckoutApiResp(checkoutResp: CheckOutResp) {
        if (Validation.isValidStatus(checkoutResp)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            this.checkoutResp = checkoutResp
            updateCheckOutApiStatus()

        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)


    }

    private fun updateCheckOutApiStatus() {
        CommonUtils.clearSharedPrefernceData()
        checkoutResp?.apply {

            SharedPreferenceManager.saveCartData(null)
            CommonUtils.showCustomToast(mContext, checkoutResp!!.message!!, 1)
            navigateToHomeActivtyScreen()

        }


    }

    fun navigateToHomeActivtyScreen() {
        val bundle = Bundle()
        bundle.putString(Constants.SOURCE, Constants.SOURCE_CART_SUMMARY)
        bundle.putString(Constants.PAYMENT_TPE, paymentType)
        CommonUtils.startActivity(this, HomeActivity::class.java, bundle, false)
        finish()
    }


    private fun setupPaymentMethod() {
        paymentType?.apply {
            if (paymentType!!.contentEquals(Constants.PAYMENT_TPE_ONLINE))
                setupRazerPayPayment()

            if (paymentType!!.contentEquals(Constants.PAYMENT_TPE_COD))
                setupCheckoutApi()

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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onPaymentError(p0: Int, msg: String?) {
        //showMsg(msg)
        CommonUtils.showCustomToast(mContext, msg!!, 1)

    }

    override fun onPaymentSuccess(msg: String?) {
        setupCheckoutApi()


    }

}
