package com.freshbasket.customer.screens.addressselection

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.widget.LinearLayout
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.constants.Constants.Companion.HOME_SCREEN
import com.freshbasket.customer.constants.Constants.Companion.MY_ACCOUNT_SCREEN
import com.freshbasket.customer.constants.Constants.Companion.NAVIGATION_FRAGMENT
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.DeliveryAddRes
import com.freshbasket.customer.model.inputmodel.Address
import com.freshbasket.customer.screens.adddeliveryaddress.DeliveryAddressActivity
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.cartsummary.CartSummaryActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.screens.location.LocationActivity
import com.freshbasket.customer.util.*
import kotlinx.android.synthetic.main.activity_delivery_address.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.tool_bar_with_back.*

class SelectAddressActivity : SubBaseActivity(), IAdapterClickListener, SelectAddressContract.View {

    private val TAG = "SelectAddressActivity"
    lateinit var selectAddressAdapter: BaseRecAdapter
    private var mContext: Context? = null
    private lateinit var selectAddressPresenter: SelectAddressPresenter
    private var addressList: ArrayList<Address>? = null
    private lateinit var snackbbar: View
    private lateinit var source: String
    private var sourceType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_delivery_address, fragment_layout)
        setToolBarTittle(getString(R.string.selectAddress))

        mContext = this
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            MyLogUtils.d(TAG, source)
        }
        hideSoftKeyboard()
        hideCartView()
        showAddAddressView()
        init()
    }

    override fun init() {
        selectAddressPresenter = SelectAddressPresenter(this)
        tv_check_continue.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_right_arrow), null)

        hideCartView()
        showAddAddressView()
        ic_add_address.setOnClickListener {
            navigateAddDeliveryAdressScreen()
        }
        tv_check_continue.setOnClickListener {
            startActivity(Intent(this, CartSummaryActivity::class.java))
        }

        setTotalPrice()

        if (source.contentEquals(NAVIGATION_FRAGMENT) || source.contentEquals(HOME_SCREEN) ||
                source.contentEquals(MY_ACCOUNT_SCREEN) || tv_mrp_price.text.isEmpty()) {
            bottom_layout_select_address.visibility = View.GONE
        }


        setupSelectAddressRecyclerView()
        snackbbar = snack_barview
        error_msg.text = getString(R.string.add_address)
        empty_button.setOnClickListener { callAddressListApi() }
        error_button.setOnClickListener { callAddressListApi() }

    }

    override fun onResume() {
        super.onResume()
        callAddressListApi()
    }

    private fun setTotalPrice() {
        SharedPreferenceManager.getCartData()?.apply {
            if (!cart_count.isNullOrEmpty() && cart_count!!.toInt() > 0) {
                tv_mrp_price.text = CommonUtils.getRupeesSymbol(context, selling_price)

            }
        }

    }

    override fun callAddressListApi() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            selectAddressPresenter.callAddressListApi(Constants.GET, CommonUtils.getCustomerID())
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    private fun selectDeliveryAddressApi(item: Address) {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            selectAddressPresenter.selectAddressApi(Constants.UPDATE, CommonUtils.getCustomerID(), item._id!!, "1")
        } else {
            showMsg(getString(R.string.error_no_internet))
        }
    }

    private fun deleteAddressApi(item: Address) {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            selectAddressPresenter.deleteAddressApi(Constants.DELETE, item._id!!, CommonUtils.getCustomerID())
        } else {
            showMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setAddressListApiRes(res: DeliveryAddRes) {
        if (Validation.isValidStatus(res)) {
            if (res.result.isNotEmpty()) {
                addressList = res.result
                showViewState(MultiStateView.VIEW_STATE_CONTENT)
                setData()
            } else {
                showViewState(MultiStateView.VIEW_STATE_EMPTY)
            }
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    override fun setSelectAddressApiResp(selectAddResp: DeliveryAddRes) {
        if (Validation.isValidStatus(selectAddResp)) {
            if (selectAddResp.result.isNotEmpty()) {
                addressList = selectAddResp.result
                showViewState(MultiStateView.VIEW_STATE_CONTENT)
                setData()
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    override fun setdelAddressApiRes(res: DeliveryAddRes) {
        if (Validation.isValidStatus(res)) {
            callAddressListApi()
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {
        setupListAddress()
    }

    private fun setupListAddress() {
        selectAddressAdapter = BaseRecAdapter(mContext!!, R.layout.items_delivery_address, adapterType = Constants.TYPE_ADDRESS_LIST_ADAPTER, adapterClickListener = this)
        rv_delivery_address.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_delivery_address.adapter = selectAddressAdapter
        rv_delivery_address.setHasFixedSize(true)
        if (addressList!!.isNotEmpty()) {
            selectAddressAdapter.addList(addressList!!)
        }
    }

    private fun setupSelectAddressRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        selectAddressAdapter = BaseRecAdapter(this, R.layout.items_delivery_address, adapterType = Constants.TYPE_SELECT_ADDRESS_ADAPTER, adapterClickListener = this)
        rv_delivery_address.apply {
            layoutManager = linearLayout
//            addItemDecoration(InsetDecoration(context))
            adapter = selectAddressAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (Validation.isValidObject(item) && item is Address) {
            when (type) {
                Constants.LIST_ADDRESS -> {
                    if (item.selected == 0) {
                        selectDeliveryAddressApi(item)
                    } else
                        showMsg("address already selected")
                }
                Constants.EDIT -> {
                    val intent = Intent(this, DeliveryAddressActivity::class.java)
                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_EDITADDRESS)
                    if (source == Constants.ORDER_NOW_BOTTOM_SHEET_FRAGMENT || source == Constants.ORDER_TYPE)
                        intent.putExtra(Constants.SOURCE_TYPE, Constants.ORDER_TYPE)
                    intent.putExtra(Constants.UPDATE_ADDRESS_DATA, item)
                    startActivityForResult(intent, Constants.EDIT_ADDRESS)
                    finish()
                }
                Constants.DELETE -> {
                    CommonUtils.showConfirmationDialog(this, this, getString(R.string.do_u_want_to_delete), item, pos)
                }
                Constants.DELETE_CONFIRMATION -> {
                    deleteAddressApi(item)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.ADD_ADDRESS -> {
                    callAddressListApi()
                }
                Constants.EDIT_ADDRESS -> {
                    callAddressListApi()
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

    private fun navigateAddDeliveryAdressScreen() {
        val intent = Intent(context, LocationActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.SOURCE_SELECT_ADDRESS)
            if (source == Constants.ORDER_NOW_BOTTOM_SHEET_FRAGMENT || source == Constants.ORDER_TYPE)
                intent.putExtra(Constants.SOURCE_TYPE, Constants.ORDER_TYPE)
            startActivityForResult(intent, Constants.ADD_ADDRESS)
        }
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