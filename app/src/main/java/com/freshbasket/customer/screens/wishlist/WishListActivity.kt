package com.freshbasket.customer.screens.wishlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
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
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.Product
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.ProductListAdapter
import com.freshbasket.customer.screens.productdetail.ProductDetailsActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.SharedPreferenceManager
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_wish_list.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class WishListActivity : SubBaseActivity(), IAdapterClickListener, WishListContract.View {

    private var modifiedProd: Product? = null
    private val TAG = "WishListActivity"
    private var mContext: Context? = null
    private lateinit var source: String
    private lateinit var wishListAdapter: ProductListAdapter
    private lateinit var snackbbar: View

    private lateinit var presenter: WishListPresenter
    private lateinit var wishlist: WishListResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_wish_list, fragment_layout)
        setToolBarTittle(getString(R.string.wishList))
        mContext = this

        tv_pre_order.setOnClickListener {
            Toast.makeText(mContext, getString(R.string.need_to_implement), Toast.LENGTH_SHORT).show()

        }
        tv_order_now.setOnClickListener {
            Toast.makeText(mContext, getString(R.string.need_to_implement), Toast.LENGTH_SHORT).show()

        }


        init()
    }


    override fun init() {
        tv_wish_list_header.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_right_arrow_black), null)
        // product_name.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_favorite_icon), null)


        snackbbar = this.snack_barview
        error_msg.text = getString(R.string.no_product)
        empty_button.setOnClickListener { callWishListApi() }
        error_button.setOnClickListener { callWishListApi() }
        presenter = WishListPresenter(this)

    }

    override fun onResume() {
        super.onResume()
        callWishListApi()
    }

    override fun callWishListApi() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callWishListApi(Constants.WISHLIST_GET)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setWishListApiRes(wishlistRes: WishListResponse) {
        if (Validation.isValidObject(wishlistRes)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            wishlist = wishlistRes
            setData()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {
        setupWishListRecyclerView()
        wishlist.result.apply {
            if (wishlist.result.isNotEmpty()) {
                wishListAdapter.addList(wishlist.result)
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)
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

    private fun setupWishListRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        wishListAdapter = ProductListAdapter(context = mContext!!, adapterClickListener = this, adapterType = Constants.TYPE_WISH_LIST_ADAPTER)
        rv_orders_cart.apply {
            layoutManager = linearLayout
            adapter = wishListAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun setWishListModifiedRes(wishlistRes: WishListResponse) {
        hideLoader()
        if (wishlistRes.isSuccess()) {
            wishListAdapter.showModifiedWishListRes(Constants.RES_SUCCESS)
        } else
            wishListAdapter.showModifiedWishListRes(Constants.RES_FAILED)


    }

    override fun modifycartApiRes(productlistRes: FetchCartResp?) {
        hideLoader()
        if (productlistRes!!.isSuccess()) {
            SharedPreferenceManager.saveCartData(productlistRes.summary)
            wishListAdapter.showModifiedRes(Constants.RES_SUCCESS)

        } else
            wishListAdapter.showModifiedRes(Constants.RES_FAILED)

    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (item is Product && item.selSku?._id != null) {
            modifiedProd = item
            when (op) {
                Constants.OP_PROD_DETAILS -> {
                    if (NetworkStatus.isOnline2(context)) {
                        val intent = Intent(mContext, ProductDetailsActivity::class.java)
                        intent.putExtra(Constants.KEY_PROD_ID, item.selSku?._id!!)
                        startActivity(intent)
                    } else
                        showMsg(getString(R.string.error_no_internet))


                }

                Constants.OP_MODIFY_CART -> {
                    if (NetworkStatus.isOnline2(context)) {
                        showLoader()
                        presenter.modifyCart(item.modifyCartIp!!)
                    } else
                        showMsg(getString(R.string.error_no_internet))

                }

                Constants.WISHLIST -> {
                    if (modifiedProd!!.isWishListEnabled()) {
                        if (NetworkStatus.isOnline2(context)) {
                            showLoader()
                            presenter.modifyWishList(if (modifiedProd?.wishlist == 0) Constants.WISHLIST_CREATE else Constants.WISHLIST_DELETE, item._id!!)

                        } else
                            showMsg(getString(R.string.error_no_internet))

                    }
                }
                /* Constants.OP_REMOVE_FROM_CART -> productListPresenter.removeFromCart(item.modifyCartIp!!)*/
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
}