package com.freshbasket.customer.screens.filterproductlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.Product
import com.freshbasket.customer.model.ProductResp
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.ProductListAdapter
import com.freshbasket.customer.screens.filtersactivity.FilterItemsActivity
import com.freshbasket.customer.screens.productdetail.ProductDetailsActivity
import com.freshbasket.customer.util.*
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class ProductListFilterActivity : SubBaseActivity(), IAdapterClickListener, ProductListFilterContract.View {
    private lateinit var snackbbar: View
    private lateinit var presenterProductList: ProductListFilterPresenter
    private lateinit var productListAdapter: ProductListAdapter
    private var productList: ArrayList<Product>? = null
    private var subCatId: String? = null
    private var modifiedProd: Product? = null
    private var mContext: Context? = null
    private val TAG = "ProductListFilterActivity"
    private lateinit var source: String
    private var curProductResp: ProductResp? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_filter, fragment_layout)
        setToolBarTittle(getString(R.string.app_name))
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            subCatId = intent.getStringExtra(Constants.ID_CATEGORY)

            MyLogUtils.d(TAG, source)
            setToolBarTittle(if (intent.getStringExtra(Constants.PARENT_CATE_NAME) != null) intent.getStringExtra(Constants.PARENT_CATE_NAME) else getString(R.string.products))
        }

        hideSearchView()
        filter_searchview.visibility = View.GONE
        showMenu()
        hideCartView()
        hideSoftKeyboard()
        init()


    }

    override fun init() {
        mContext = this
        presenterProductList = ProductListFilterPresenter(this)
        snackbbar = snack_barview

        tv_filter.setCompoundDrawablesWithIntrinsicBounds((AppCompatResources.getDrawable(this, R.drawable.ic_filter)), null, null, null)
        tv_sort_by_price.setCompoundDrawablesWithIntrinsicBounds(null, null, (AppCompatResources.getDrawable(this, R.drawable.ic_sort_by)), null)

        empty_button.setOnClickListener { callProductListApi() }
        error_button.setOnClickListener { callProductListApi() }

        tv_filter.setOnClickListener {
            navigateFilterItemsScreen()
        }


        tv_sort_by_price.setOnClickListener { setupSortedProductList() }
        // setupProductListFragmentRecyclerView()
        callProductListApi()
    }

    private fun setupSortedProductList() {

        tv_sort_by_price.setCompoundDrawablesWithIntrinsicBounds(null, null, (AppCompatResources.getDrawable(this, R.drawable.ic_sort_by)), null)

        productListAdapter.addList(curProductResp!!.getSortedProductData())
    }

    override fun onResume() {
        super.onResume()
        callProductListApi()
    }


    private fun setupProductListFragmentRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        productListAdapter = ProductListAdapter(context = this, adapterClickListener = this, adapterType = Constants.TYPE_PRODUCT_LIST_ADAPTER)
        rv_filterd_list.apply {
            layoutManager = linearLayout
            adapter = productListAdapter
            isNestedScrollingEnabled = false
        }

    }

    override fun callProductListApi() {
        if (NetworkStatus.isOnline2(mContext) && Validation.isValidString(subCatId)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenterProductList.getProductList(subCatId!!)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    private fun navigateFilterItemsScreen() {
        val intent = Intent(this, FilterItemsActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.TYPE_FILTER_ADAPTER)
            putExtra(Constants.ID_CATEGORY,subCatId)
        }
        startActivity(intent)
    }

    override fun setProductListApiResp(productlistRes: ProductResp) {
        if (productlistRes.isSuccess()) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            productlistRes.product?.apply {
                if (productlistRes.product.isNotEmpty()) {
                    curProductResp = productlistRes
                    setupProductListFragmentRecyclerView()
                    productListAdapter.addList(productlistRes.product)
                } else
                    showViewState(MultiStateView.VIEW_STATE_EMPTY)
            }
        } else {

            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }

    }


    override fun modifycartApiRes(productlistRes: FetchCartResp?) {
        hideLoader()
        if (productlistRes!!.isSuccess()) {
            SharedPreferenceManager.saveCartData(productlistRes.summary)
            productListAdapter.showModifiedRes(Constants.RES_SUCCESS)

        } else
            productListAdapter.showModifiedRes(Constants.RES_FAILED)

    }

    override fun setWishListApiRes(wishlistRes: WishListResponse) {
        hideLoader()
        if (wishlistRes.isSuccess()) {
            productListAdapter.showModifiedWishListRes(Constants.RES_SUCCESS)
        } else
            productListAdapter.showModifiedWishListRes(Constants.RES_FAILED)
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
                        presenterProductList.modifyCart(item.modifyCartIp!!)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                }
                Constants.WISHLIST -> {
                    if (NetworkStatus.isOnline2(context)) {
                        showLoader()
                        presenterProductList.modifyWishList(if (modifiedProd?.wishlist == 0) Constants.WISHLIST_CREATE else Constants.WISHLIST_DELETE, item._id!!)

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


}
