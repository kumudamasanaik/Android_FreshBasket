package com.freshbasket.customer.screens.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import com.freshbasket.customer.MyApplication.Companion.context
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.Product
import com.freshbasket.customer.model.SearchProductRes
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.model.inputmodel.SearchInput
import com.freshbasket.customer.screens.commonadapter.ProductListAdapter
import com.freshbasket.customer.screens.productdetail.ProductDetailsActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.SharedPreferenceManager
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : AppCompatActivity(), SearchContract.View, IAdapterClickListener {
    private lateinit var searchListAdapter: ProductListAdapter
    internal lateinit var searchChar: String
    internal lateinit var input: String
    private lateinit var source: String
    private lateinit var cartList: FetchCartResp
    private var modifiedProd: Product? = null
    private var mContext: Context? = null
    private lateinit var searchRes: SearchProductRes
    lateinit var presenter: SearchPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        init()

    }

    override fun init() {
        presenter = SearchPresenter(this)
        mContext = this
        et_text.addTextChangedListener(object : TextWatcher {
            private var timer = Timer()
            private val DELAY: Long = 750 // milliseconds

            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(searchChar: CharSequence, start: Int, count: Int, after: Int) {
                timer.cancel()
                timer = Timer()
                timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                Looper.prepare()

                                if (searchChar.toString().isNotEmpty()) {
                                    this@SearchActivity.searchChar = searchChar.toString()
                                    input = searchChar.toString()
                                    searchProducts()
                                }
                                Looper.loop()
                            }

                        },
                        DELAY
                )

            }

            override fun afterTextChanged(s: Editable) {
            }


        })
    }

    override fun searchProducts() {

        var input = SearchInput(_id = CommonUtils.getCustomerID(), _session = CommonUtils.getSession(), count = 100,
                lang = "en", search = input, start = 0)
        if (NetworkStatus.isOnline2(this)) {
            presenter.callSearch(input)
        } else {
            showMsg(getString(R.string.error_no_internet))
        }
    }


    private fun setupListProducts() {
        searchListAdapter = ProductListAdapter(this, adapterType = Constants.TYPE_CART_ADAPTER, adapterClickListener = this)
        rc_search.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rc_search.adapter = searchListAdapter
        rc_search.setHasFixedSize(true)
        if (Validation.isValidList(searchRes.product!!))
            searchListAdapter.addList(searchRes.product!!)
    }


    override fun setSearchProductsRes(res: SearchProductRes) {
        if (Validation.isValidStatus(res)) {
            if (Validation.isValidList(res.product)) {
                searchRes = res
                setupListProducts()
            } else {
                showMsg("Products are not available")
            }
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
                searchListAdapter.addList(cartList.cart!!)
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)

        }
        val totalPrice = cartList.summary!!.grand_total
        val cart_summary = cartList.summary
        cart_summary?.apply {
            SharedPreferenceManager.saveCartData(cart_summary)
            tv_price_amount.text = CommonUtils.getRupeesSymbol(mContext!!, selling_price)
        }
    }


    override fun modifycartApiRes(productlistRes: FetchCartResp?) {
        hideLoader()
        if (Validation.isValidStatus(productlistRes)) {
            if (productlistRes!!.isSuccess()) {
                SharedPreferenceManager.saveCartData(productlistRes.summary)
                searchListAdapter.showModifiedRes(Constants.RES_SUCCESS)
                // callCartApi()
                showViewState(MultiStateView.VIEW_STATE_CONTENT)
            } else
                searchListAdapter.showModifiedRes(Constants.RES_FAILED)

        }

    }

    override fun setWishListApiRes(wishlistRes: WishListResponse) {
        hideLoader()
        if (wishlistRes.isSuccess()) {
            searchListAdapter.showModifiedWishListRes(Constants.RES_SUCCESS)
        } else
            searchListAdapter.showModifiedWishListRes(Constants.RES_FAILED)
    }

    override fun showMsg(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    }

    override fun showLoader() {
        CommonUtils.showLoading(this, true)

    }

    override fun hideLoader() {
        CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {

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


                /* Constants.OP_REMOVE_FROM_CART -> productListPresenter.removeFromCart(item.modifyCartIp!!)*/
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                hideSoftKeyboard()
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}
