package com.freshbasket.customer.screens.coupon

import android.content.Context
import android.content.Intent
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
import com.freshbasket.customer.model.ApplyCouponRes
import com.freshbasket.customer.model.CouponRes
import com.freshbasket.customer.model.CouponResData
import com.freshbasket.customer.model.inputmodel.PromoCodeInput
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.cartsummary.CartSummaryActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_coupon.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class CouponActivity : SubBaseActivity(), CouponContract.View, IAdapterClickListener {


    lateinit var mContext: Context
    private lateinit var snackbbar: View
    private lateinit var presenter: CouponPresenter
    private lateinit var couponAdapter: BaseRecAdapter
    private var orderId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_coupon, fragment_layout)
        setToolBarTittle(getString(R.string.apply_coupon))

        intent?.extras?.apply {
            orderId = getInt(Constants.ORDER_ID)
        }

        init()
    }

    override fun init() {
        mContext = this
        snackbbar = snack_barview
        presenter = CouponPresenter(this)
        empty_button.setOnClickListener { }
        error_button.setOnClickListener { }
        setupCartRecyclerView()
        hideCartView()
        callCouponList()
    }

    override fun callCouponList() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callCouponListApi()

        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setCouponListResp(res: CouponRes) {
        if (Validation.isValidList(res.result)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            couponAdapter.addList(res.result!!)
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    override fun setPromoCodeResp(promoRes: ApplyCouponRes) {
        if (Validation.isValidObject(promoRes)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            val intent = Intent(this, CartSummaryActivity::class.java)
            intent.putExtra(Constants.PROMO_CODE_AMT, promoRes.discount)
            startActivity(intent)
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }


    private fun setupCartRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        couponAdapter = BaseRecAdapter(context, R.layout.items_coupon_list, adapterClickListener = this)
        rv_coupon.apply {
            layoutManager = linearLayout
            adapter = couponAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (item is CouponResData && item._id != null) {
            when (op) {
                Constants.APPLY_PROMO_CODE -> {
                    val promoCode = item.promo_code
                    if (NetworkStatus.isOnline2(context)) {
                        showLoader()
                        presenter.callPromoCodeApi(promoCodeInput = PromoCodeInput(id_order = orderId, _session = CommonUtils.getSession(), _id = CommonUtils.getCustomerID(), promocode = promoCode))
                    } else
                        showMsg(getString(R.string.error_no_internet))
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

