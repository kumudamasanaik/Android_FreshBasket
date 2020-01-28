package com.freshbasket.customer.screens.shopbycategory

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
import com.freshbasket.customer.model.Category
import com.freshbasket.customer.model.SubCategory
import com.freshbasket.customer.model.SubCategoryResp
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.screens.productlist.ProductListActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.MyLogUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_shop_by_category.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class ShopByCategoryActivity : SubBaseActivity(), IAdapterClickListener, ShopByCategoryContract.View {
    private val TAG = "ShopByCategoryActivity"
    private var mContext: Context? = null
    lateinit var catname: String
    private lateinit var source: String
    private lateinit var snackbbar: View
    private lateinit var presenter: ShopByCategoryPresenter
    private lateinit var shopByCategory: SubCategoryResp
    private lateinit var shopbycategoryAdpater: BaseRecAdapter
    private lateinit var msubCategory: SubCategory
    private lateinit var category: SubCategory
    private var parentCatIID: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_shop_by_category, fragment_layout)
        setToolBarTittle(getString(R.string.shopByCategory))

        mContext = this
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            MyLogUtils.d(TAG, source)
        }
        hideCartView()
        init()
    }

    override fun init() {
        presenter = ShopByCategoryPresenter(this)
        setupShopCategoryRecyclerView()
        snackbbar = snack_barview
        empty_button.setOnClickListener { callShopByCategoryApi() }
        error_button.setOnClickListener { callShopByCategoryApi() }
        callShopByCategoryApi()
    }

    override fun callShopByCategoryApi() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callSubCategoryApi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setSubCategoryApiRes(subCategoryResp: SubCategoryResp) {
        if (Validation.isValidStatus(subCategoryResp) && Validation.isValidObject(subCategoryResp.result)) {
            shopByCategory = subCategoryResp
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            setData()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {
        if (shopByCategory.result!!.isNotEmpty()) {
            shopbycategoryAdpater.addList(shopByCategory.result!!)
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

    private fun setupShopCategoryRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        shopbycategoryAdpater = BaseRecAdapter(this, R.layout.partialy_shop_by_category_list_item, adapterType = Constants.TYPE_SHOP_BY_CATEGORY_ADAPTER, adapterClickListener = this)
        rv_shop_by_category.apply {
            layoutManager = linearLayout
            adapter = shopbycategoryAdpater
            isNestedScrollingEnabled = false
        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {

        if (item is SubCategory && Validation.isValidObject(item)) {
            item.apply {
                // category = item
                parentCatIID = type
                if (Validation.isValidObject(CommonUtils.getParentCategoryData(type))) {
                    val category = CommonUtils.getParentCategoryData(type) as Category
                    val intent = Intent(context, ProductListActivity::class.java)
                    intent.apply {
                        putExtra(Constants.SOURCE, Constants.SOURCE_SIGN)
                        intent.putExtra(Constants.KEY_CAT_DATA, category)
                        intent.putExtra(Constants.PARENT_CAT_ID, category._id)
                        intent.putExtra(Constants.PARENT_CAT_NAME, category.name)
                        intent.putExtra(Constants.POS, pos)
                    }
                    startActivity(intent)
                }

            }

        }

    }

    private fun navigateProductListScreen(pos: Int) {
        val intent = Intent(context, ProductListActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.SOURCE_SIGN)
            intent.putExtra(Constants.POS, pos)
            intent.putExtra(Constants.PARENT_CAT_ID, parentCatIID)
            intent.putExtra(Constants.PARENT_CAT_NAME, catname)
            startActivity(intent)


        }
        startActivity(intent)
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