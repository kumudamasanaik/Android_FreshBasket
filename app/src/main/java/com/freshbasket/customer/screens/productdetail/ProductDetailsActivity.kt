package com.freshbasket.customer.screens.productdetail

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.ProductDetailsResp
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.ViewPagerAdapter
import com.freshbasket.customer.screens.productdetail.fragment.ProductDetailFragment
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.ImageLoader
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showToastMsg
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.partial_tab_layout.*

class ProductDetailsActivity : SubBaseActivity(), ProductDetailsContract.View, ViewPager.OnPageChangeListener {


    private lateinit var mContext: Context
    private lateinit var pagerAdapetr: ViewPagerAdapter
    private lateinit var presenter: ProductDetailsPresenter
    private lateinit var productDetailsRes: ProductDetailsResp
    private lateinit var product_id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_product_details, fragment_layout)
        mContext = this

        if (intent != null) {
            product_id = intent.getStringExtra(Constants.KEY_PROD_ID)
        }
        init()

    }

    override fun init() {
        presenter = ProductDetailsPresenter(this)
        error_button.setOnClickListener { callProductDetails() }
        empty_button.setOnClickListener { callProductDetails() }
        callProductDetails()

    }

    override fun callProductDetails() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callProductDetailsApi("130")

        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setProductDetailsData(res: ProductDetailsResp) {
        if (Validation.isValidObject(res)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            productDetailsRes = res
            tv_combo_pack.text = res.product!![0].name
            tv_details.text = res.product[0].description
            tv_price_amount.text = CommonUtils.getRupeesSymbol(mContext, res.product[0].sku!![0].selling_price.toString())
            ImageLoader.setImage(image_smart_basket, res.product[0].pic!![0].pic!!)

            setupViewPager()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }


    private fun setupViewPager() {
        pagerAdapetr = ViewPagerAdapter(supportFragmentManager)
        setupHeaderList()
        cat_pager.adapter = pagerAdapetr
        tab_layout.setupWithViewPager(cat_pager)
        cat_pager.addOnPageChangeListener(this)
    }

    private fun setupHeaderList() {
        if (productDetailsRes.product!![0].spec!!.isNotEmpty()) {

            for (ProductListHeader in productDetailsRes.product!![0].spec!!) {
                pagerAdapetr.addList(ProductDetailFragment.newInstance(productDetailsRes.product!![0].spec!!), ProductListHeader.specification!!)
            }
        }
    }


    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        pagerAdapetr.getItem(position)
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


