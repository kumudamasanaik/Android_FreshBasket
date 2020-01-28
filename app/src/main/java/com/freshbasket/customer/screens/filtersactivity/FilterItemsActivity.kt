package com.freshbasket.customer.screens.filtersactivity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.model.NewFilterResUpdated
import com.freshbasket.customer.model.Refine
import com.freshbasket.customer.model.Sort
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.ViewPagerAdapter
import com.freshbasket.customer.screens.filtersactivity.refinedfragment.RefinedFragment
import com.freshbasket.customer.screens.filtersactivity.sortfragment.SortByFragment
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.partial_swiping_tab_layout.*
import java.util.*

class FilterItemsActivity : SubBaseActivity(), NewFilterContract.View, View.OnClickListener {

    private lateinit var pagerAdapetr: ViewPagerAdapter
    private lateinit var snackbbar: View
    private lateinit var presenter: NewFilterPresenter
    private lateinit var filterRes: NewFilterResUpdated
    lateinit var refineArrayList: ArrayList<Refine>
    lateinit var sortArrayList: ArrayList<Sort>
    private var category_Id: String? = null
    private lateinit var source: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_new_filter, fragment_layout)
        setToolBarTittle(getString(R.string.app_name))

        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            category_Id = intent.getStringExtra(Constants.ID_CATEGORY)
        }
        init()
        //showBack()
    }

    override fun init() {
        presenter = NewFilterPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { callFilterApi() }
        error_button.setOnClickListener { callFilterApi() }
        callFilterApi()
    }

    override fun callFilterApi() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callFilterApi("8")
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setFilterApiResp(res: NewFilterResUpdated) {
        if (Validation.isValidStatus(res)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            filterRes = res
            refineArrayList = filterRes.refine!!
            sortArrayList = filterRes.sort!!
            setUpTabLayout()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }


    private fun setUpTabLayout() {
        pagerAdapetr = ViewPagerAdapter(supportFragmentManager)
        pagerAdapetr.apply {
            addList(RefinedFragment(), getString(R.string.refining_fragment))
            addList(SortByFragment(), getString(R.string.sort_fragment))
        }
        filter_view_pager.adapter = pagerAdapetr
        swiping_tab_layout.setupWithViewPager(filter_view_pager)
    }

    override fun showMsg(msg: String?) {

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

    override fun onClick(p0: View?) {

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