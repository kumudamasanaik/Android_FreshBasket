package com.freshbasket.customer.screens.offers

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
import com.freshbasket.customer.constants.Constants.Companion.TYPE_OFFERS_ADAPTER
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.OfferResp
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.screens.offersdetails.OfferDetailActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.MyLogUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_offers.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class OffersActivity : SubBaseActivity(), IAdapterClickListener, OffersContract.View {
    private val TAG = "OffersActivity"

    lateinit var mContext: Context
    private lateinit var snackbbar: View
    private lateinit var source: String
    lateinit var offersAdapter: BaseRecAdapter
    private lateinit var presenter: OffersPresenter
    private lateinit var offerList: OfferResp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_offers, fragment_layout)
        setToolBarTittle(getString(R.string.offer))
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE) ?: " "
            MyLogUtils.d(TAG, source)
        }
        init()
    }

    override fun init() {
        presenter = OffersPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { callOffers() }
        error_button.setOnClickListener { callOffers() }
        setupOfferRecyclerView()
        hideCartView()
        callOffers()

    }

    override fun callOffers() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callOffersApi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setOffersResp(res: OfferResp) {
        if (Validation.isValidStatus(res) && Validation.isValidObject(res)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            offerList = res
            if (Validation.isValidList(res.sku_details)) {
                setupData()
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)

        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setupOfferRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        offersAdapter = BaseRecAdapter(context = this, type = R.layout.partial_offers_list_item, adapterClickListener = this, adapterType = Constants.TYPE_OFFERS_ADAPTER)
        rv_offers_list.apply {
            layoutManager = linearLayout
            adapter = offersAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupData() {
        if (Validation.isValidList(offerList.sku_details))
            offersAdapter.addList(offerList.sku_details!!)
    }


    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (op == TYPE_OFFERS_ADAPTER) {
            val intent = Intent(context, OfferDetailActivity::class.java)
            intent.apply {
                putExtra(Constants.SOURCE, Constants.OFFERS_SCREEN)
                putExtra(Constants.ID_OFFER, offerList.sku_details!![0]._id)
            }
            startActivity(intent)
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
