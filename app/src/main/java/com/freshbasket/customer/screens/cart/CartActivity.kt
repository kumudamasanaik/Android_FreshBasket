package com.freshbasket.customer.screens.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.BottomSheetFragment
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.customviews.OrderNowBottomSheetFragment
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.Product
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.ProductListAdapter
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.screens.productdetail.ProductDetailsActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.SharedPreferenceManager
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class CartActivity : SubBaseActivity(), IAdapterClickListener, CartContract.View {


    private val TAG = "CartActivity"
    lateinit var mContext: Context
    private lateinit var source: String
    private lateinit var snackbbar: View
    private lateinit var presenter: CartPresenter
    private lateinit var cartList: FetchCartResp
    lateinit var cartAdapter: ProductListAdapter
    private var modifiedProd: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_cart, fragment_layout)
        setToolBarTittle(getString(R.string.cart))
        /*if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            MyLogUtils.d(TAG, source)

        }*/
        init()
    }

    override fun init() {
            mContext = this
            snackbbar = snack_barview
            presenter = CartPresenter(this)
            tv_cart_header.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_right_arrow_black), null)
            empty_button.text = getString(R.string.add_cart)
            error_msg.text = getString(R.string.cart_empty)
            empty_button.setOnClickListener { navigateHomeScreen() }
            error_button.setOnClickListener { callCartApi() }
            setupCartRecyclerView()
            hideCartView()
            callCartApi()


        tv_order_now.setOnClickListener {
            val ordernowbottomSheetFragment = OrderNowBottomSheetFragment()
            ordernowbottomSheetFragment.show(supportFragmentManager, ordernowbottomSheetFragment.tag)
        }

        tv_pre_order.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun navigateHomeScreen() {

        val intent = Intent(this, HomeActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.HOME_CART_LIST)
        }
        startActivity(intent)
    }

    override fun callCartApi() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callCartApi(Constants.CART_GET)

        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setCartApiResp(cartres: FetchCartResp) {
        if (Validation.isValidObject(cartres)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            cartList = cartres
            setData()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)

    }


    private fun setData() {
        cartList.cart?.apply {
            if (cartList.cart!!.isNotEmpty()) {
                cartAdapter.addList(cartList.cart!!)
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)

        }


        val totalPrice = cartList.summary!!.grand_total
        val cart_summary = cartList.summary
        cart_summary?.apply {
            SharedPreferenceManager.saveCartData(cart_summary)
            tv_price_amount.text = CommonUtils.getRupeesSymbol(mContext, selling_price)
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

    private fun setupCartRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        cartAdapter = ProductListAdapter(context = this, adapterClickListener = this, adapterType = Constants.TYPE_CART_ADAPTER)
        rv_cart.apply {
            layoutManager = linearLayout
            adapter = cartAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun modifycartApiRes(productlistRes: FetchCartResp?) {
        hideLoader()
        if (Validation.isValidStatus(productlistRes)) {
            if (productlistRes!!.isSuccess()) {
                SharedPreferenceManager.saveCartData(productlistRes.summary)
                cartAdapter.showModifiedRes(Constants.RES_SUCCESS)
                callCartApi()
                showViewState(MultiStateView.VIEW_STATE_CONTENT)
            } else
                cartAdapter.showModifiedRes(Constants.RES_FAILED)

        }

    }

    override fun setWishListApiRes(wishlistRes: WishListResponse) {
        hideLoader()
        if (wishlistRes.isSuccess()) {
            cartAdapter.showModifiedWishListRes(Constants.RES_SUCCESS)
        } else
            cartAdapter.showModifiedWishListRes(Constants.RES_FAILED)
    }


    override fun setRemoveCartApiRes(productlistRes: FetchCartResp?) {
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

                Constants.OP_MODIFY_CART ->
                    if (NetworkStatus.isOnline2(context)) {
                        showLoader()
                        presenter.modifyCart(item.modifyCartIp!!)
                    } else
                        showMsg(getString(R.string.error_no_internet))

                Constants.WISHLIST ->
                    if (NetworkStatus.isOnline2(context)) {
                        showLoader()
                        presenter.modifyWishList(if (modifiedProd?.wishlist == 0) Constants.WISHLIST_CREATE else Constants.WISHLIST_DELETE, item.selSku!!.id_product!!)

                    } else
                        showMsg(getString(R.string.error_no_internet))


                Constants.TYPE_REMOVE_CART ->
                    if (NetworkStatus.isOnline2(context)) {
                        showLoader()
                        presenter.callRemoveCartApi(item.modifyCartIp!!)

                    } else
                        showMsg(getString(R.string.error_no_internet))
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
