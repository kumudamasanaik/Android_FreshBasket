package com.freshbasket.customer.screens.productlist

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Category
import com.freshbasket.customer.model.ProductListHeaderResp
import com.freshbasket.customer.model.SubCategory
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.ViewPagerAdapter
import com.freshbasket.customer.screens.filterproductlist.ProductListFilterActivity
import com.freshbasket.customer.screens.productlist.fragment.ProductListFragment
import com.freshbasket.customer.screens.shopbycategory.ShopByCategoryActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showToastMsg
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.partial_tab_layout.*
import kotlinx.android.synthetic.main.tool_bar_with_back.*


class ProductListActivity : SubBaseActivity(), ProductListActivtyContract.View, ViewPager.OnPageChangeListener, IAdapterClickListener {
    private lateinit var mContext: Context
    private lateinit var pagerAdapetr: ViewPagerAdapter
    private lateinit var presenter: ProductListActivityPresenter
    private lateinit var productRespData: ProductListHeaderResp
    private lateinit var parentId: String
    private lateinit var parentName: String
    lateinit var dialog: Dialog
    var passedCatData: SubCategory? = null
    private lateinit var category: Category
    private var first: Boolean = true
    private var cur_pos: Int = 0
    private lateinit var source: String
    private var childPrdoductCatId: String? = null
    private var mainCateName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_product_list, fragment_layout)
        mContext = this
        init()

    }

    override fun init() {
        presenter = ProductListActivityPresenter(this)
        mContext = this
        getIntentData()
        ShowCatDropDownView()
        showFilterIcon()
        error_button.setOnClickListener { getViewPagerHeaderData() }
        empty_button.setOnClickListener { getViewPagerHeaderData() }


        rl_title_layout.setOnClickListener {
            navigateShopByCategoryScreen()

        }

        ic_filter.setOnClickListener {

            navigateFilterScreen()

        }


        getViewPagerHeaderData()
    }


    private fun navigateShopByCategoryScreen() {
        val intent = Intent(this, ShopByCategoryActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.TYPE_PRODUCT_LIST)
        }
        startActivity(intent)
    }


    private fun navigateFilterScreen() {
        val intent = Intent(this, ProductListFilterActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.TYPE_PRODUCT_LIST)
            putExtra(Constants.ID_CATEGORY, childPrdoductCatId)
            putExtra(Constants.PARENT_CATE_NAME, mainCateName)

        }
        startActivity(intent)
    }


    private fun getIntentData() {
        intent?.apply {
            intent.extras?.apply {
                source = intent.getStringExtra(Constants.SOURCE)
                parentId = getStringExtra(Constants.PARENT_CAT_ID)
                cur_pos = getInt(Constants.POS)
                parentName = getString(Constants.PARENT_CAT_NAME)
                setToolBarTittle(parentName)

            }
        }
    }

    override fun getViewPagerHeaderData() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callChildCategoryApi(parentId)

        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }

    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {

        if (item is Category && type!!.contentEquals(Constants.TYPE_MAIN_CAT_ADAPTER)) {


            item.apply {
                setToolBarTittle(passedCatData?.name ?: item.name)
                parentId = _id!!
                cur_pos = 0
                dialog.dismiss()

                getViewPagerHeaderData()

            }

        }
    }

    override fun setViewPagerHeaderData(productlistHeaderRes: ProductListHeaderResp) {
        if (Validation.isValidStatus(productlistHeaderRes) && productlistHeaderRes.result!!.isNotEmpty()) {
            productRespData = productlistHeaderRes
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            setupViewPager()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }


    override fun showMsg(msg: String?) {
        showToastMsg(msg!!)
    }

    override fun showLoader() {
        CommonUtils.showLoading(this, true)
    }

    override fun hideLoader() {
        CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {
        base_multistateview.viewState = state
    }


    private fun setupViewPager() {
        pagerAdapetr = ViewPagerAdapter(supportFragmentManager)
        setupHeaderList()
        cat_pager.adapter = pagerAdapetr
        tab_layout.setupWithViewPager(cat_pager)
        cat_pager.currentItem = cur_pos
        cat_pager.addOnPageChangeListener(this)
    }


    private fun updateSelectedTabData(position: Int) {
        if (productRespData.result!!.isNotEmpty()) {
            val fragmnet = pagerAdapetr.getItem(position) as ProductListFragment
            mainCateName = productRespData.result!![position].name
            childPrdoductCatId = productRespData.result!![position]._id
            if (Validation.isValidObject(fragmnet)) {
                fragmnet.setupChildData(childPrdoductCatId)
            }
        }

    }

    private fun setupHeaderList() {
        if (productRespData.result!!.isNotEmpty()) {
            for (ProductListHeader in productRespData.result!!)
                pagerAdapetr.addList(ProductListFragment.newInstance(-1), ProductListHeader.name!!)

        }

    }


    override fun onPageScrollStateChanged(state: Int) {


    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (first && positionOffset == 0f && positionOffsetPixels == 0) {
            onPageSelected(cur_pos)
            first = false
        }
    }

    override fun onPageSelected(position: Int) {
        updateSelectedTabData(position)

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
