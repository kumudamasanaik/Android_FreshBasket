package com.freshbasket.customer.screens.smartbasket

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
import com.freshbasket.customer.model.ComboDetailResp
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.cart.CartActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.util.*
import kotlinx.android.synthetic.main.activity_smart_basket.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.partial_smart_basket_item.*
import kotlinx.android.synthetic.main.partial_smart_basket_product_quantity_item.*

class SmartBasketActivity : SubBaseActivity(), SmartBasketContract.View, IAdapterClickListener {
    private val TAG = "SmartBasketActivity"
    private lateinit var snackbbar: View
    lateinit var mContext: Context
    private lateinit var presenter: SmartBasketPresenter
    lateinit var smartBasketAdapter: BaseRecAdapter
    lateinit var comboDetailList: ComboDetailResp
    private lateinit var cat_id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_smart_basket, fragment_layout)
        setToolBarTittle(getString(R.string.smartBasket))

        if (intent != null) {
            cat_id = intent.getStringExtra(Constants.ID_PRODUCT)
            MyLogUtils.d(TAG, cat_id)
        }
        init()

    }

    override fun init() {
        tv_goto_cart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        presenter = SmartBasketPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { callSmartBasket() }
        error_button.setOnClickListener { callSmartBasket() }
        setUpSmartBasketRecyclerview()
        callSmartBasket()
    }


    override fun callSmartBasket() {

        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callSmartBasketApi(cat_id)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)

        }

    }

    override fun setSmartBasketResp(res: ComboDetailResp) {
        if (Validation.isValidStatus(res)) {
            if (res.combo!!.isNotEmpty()) {
                comboDetailList = res
                showViewState(MultiStateView.VIEW_STATE_CONTENT)
                setData()
            } else {
                showViewState(MultiStateView.VIEW_STATE_EMPTY)
            }
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)

    }

    private fun setData() {
        if (!comboDetailList.combo!![0].name.isNullOrEmpty())
            tv_fresh_fruits.text = comboDetailList.combo!![0].name

        if (!comboDetailList.combo!![0].description.isNullOrEmpty())
            tv_details.text = comboDetailList.combo!![0].description

        if (!comboDetailList.combo!![0].pic!![0].pic.isNullOrEmpty())
            ImageLoader.setImage(image_smart_basket, comboDetailList.combo!![0].pic!![0].pic!!)

        if (!comboDetailList.combo!![0].sku!![0].mrp.isNullOrEmpty())
            tv_price_amount.text = CommonUtils.getRupeesSymbol(context, comboDetailList.combo!![0].sku!![0].mrp!!)

        if (!comboDetailList.combo!![0].sku!![0].selling_price.isNullOrEmpty())
            tv_offers_amt.text = CommonUtils.getRupeesSymbol(context, comboDetailList.combo!![0].sku!![0].selling_price!!)

        if (Validation.isValidList(comboDetailList.combo!![0].products!!))
            smartBasketAdapter.addList(comboDetailList.combo!![0].products!!)

        if (comboDetailList.combo!![0].sku!![0].mycart!! == "0"){
            rel_cart.visibility = View.GONE
        }
    }

    private fun setUpSmartBasketRecyclerview() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        smartBasketAdapter = BaseRecAdapter(context = this, type = R.layout.recyclerview_smart_basket_items, adapterClickListener = this, adapterType = Constants.TYPE_SMART_BASKET_ADAPTER)
        rv_product_list.apply {
            layoutManager = linearLayout
            adapter = smartBasketAdapter
            isNestedScrollingEnabled = false
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
