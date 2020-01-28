package com.freshbasket.customer.screens.combo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.ComboOfferRes
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.Product
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.ProductListAdapter
import com.freshbasket.customer.screens.smartbasket.SmartBasketActivity
import com.freshbasket.customer.util.*
import kotlinx.android.synthetic.main.activity_combo.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class ComboActivity : SubBaseActivity(), ComboContract.View, IAdapterClickListener {

    private lateinit var snackbbar: View

    private val TAG = "SelectAddressActivity"
    lateinit var comboListAdapter: ProductListAdapter
    private var mContext: Context? = null
    private lateinit var source: String
    private var comboList: ComboOfferRes? = null
    private var modifiedProd: Product? = null
    private lateinit var comboOfferPresenter: ComboPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_combo, fragment_layout)
        setToolBarTittle(getString(R.string.smartBasket))

        mContext = this
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE) ?: ""
            MyLogUtils.d(TAG, source)
        }
        hideSoftKeyboard()
        init()

    }

    override fun init() {
        comboOfferPresenter = ComboPresenter(this)
        snackbbar = snack_barview
        setUpComboRecyclerView()
        empty_button.setOnClickListener { callComboListApi() }
        error_button.setOnClickListener { callComboListApi() }

    }

    override fun onResume() {
        super.onResume()
        callComboListApi()
    }

    override fun callComboListApi() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            comboOfferPresenter.callComboListApi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setComboListApiRes(res: ComboOfferRes) {
        if (Validation.isValidStatus(res)) {
            if (res.combo!!.isNotEmpty()) {
                comboList = res
                showViewState(MultiStateView.VIEW_STATE_CONTENT)
                setData()
            } else {
                showViewState(MultiStateView.VIEW_STATE_EMPTY)
            }
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setUpComboRecyclerView() {
        val linearLayout = LinearLayoutManager(context)
        linearLayout.orientation = LinearLayoutManager.VERTICAL
        comboListAdapter = ProductListAdapter(context = context, adapterClickListener = this, adapterType = Constants.TYPE_COMBO_LIST_ADAPTER)
        rv_combo_list.apply {
            layoutManager = linearLayout
            adapter = comboListAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun modifycartApiRes(productlistRes: FetchCartResp?) {
        hideLoader()
        if (productlistRes!!.isSuccess()) {
            SharedPreferenceManager.saveCartData(productlistRes.summary)
            comboListAdapter.showModifiedRes(Constants.RES_SUCCESS)

        } else
            comboListAdapter.showModifiedRes(Constants.RES_FAILED)
    }

    override fun setWishListApiRes(wishlistRes: WishListResponse) {
        hideLoader()
        if (wishlistRes.isSuccess()) {
            comboListAdapter.showModifiedWishListRes(Constants.RES_SUCCESS)
        } else
            comboListAdapter.showModifiedWishListRes(Constants.RES_FAILED)
    }

    private fun setData() {
        if (Validation.isValidList(comboList?.combo!!))
            comboListAdapter.addList(comboList?.combo!!)

    }


    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (item is Product && item.selSku?._id != null) {
            modifiedProd = item
            when (op) {
                Constants.OP_PROD_DETAILS -> {
                    if (NetworkStatus.isOnline2(context)) {
                        val intent = Intent(mContext, SmartBasketActivity::class.java)
                        intent.putExtra(Constants.ID_PRODUCT, item._id)
                        startActivity(intent)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                }

                Constants.OP_MODIFY_CART -> {
                    if (NetworkStatus.isOnline2(context)) {
                        showLoader()
                        comboOfferPresenter.modifyCart(item.modifyCartIp!!)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                }
                Constants.WISHLIST -> {
                    if (NetworkStatus.isOnline2(context)) {
                        showLoader()
                        comboOfferPresenter.modifyWishList(if (modifiedProd?.wishlist == 0) Constants.WISHLIST_CREATE else Constants.WISHLIST_DELETE, item._id!!)

                    } else
                        showMsg(getString(R.string.error_no_internet))
                }

                /* Constants.OP_REMOVE_FROM_CART -> productListPresenter.removeFromCart(item.modifyCartIp!!)*/
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
