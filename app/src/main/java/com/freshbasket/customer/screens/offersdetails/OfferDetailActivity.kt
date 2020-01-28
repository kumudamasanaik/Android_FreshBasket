package com.freshbasket.customer.screens.offersdetails

import android.content.Context
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
import com.freshbasket.customer.model.OrderDetailsResp
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.util.*
import kotlinx.android.synthetic.main.activity_offer_detail.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.partial_offers_list_item.*

class OfferDetailActivity : SubBaseActivity(), OfferDetailContract.View, IAdapterClickListener {


    private lateinit var presenter: OfferDetailPresenter
    private lateinit var source: String
    lateinit var offersDetailsAdapter: BaseRecAdapter
    private var mContext: Context? = null
    private val TAG = "OfferDetailActivity"
    private lateinit var offerDetailsList: OrderDetailsResp
    private lateinit var snackbbar: View
    private lateinit var _id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_offer_detail, fragment_layout)
        setToolBarTittle(getString(R.string.offerDetails))

        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            _id = intent.getStringExtra(Constants.ID_OFFER)
            MyLogUtils.d(TAG, source)
        }
        init()
    }

    override fun init() {
        presenter = OfferDetailPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { callOfferDeatils() }
        error_button.setOnClickListener { callOfferDeatils() }
        setupOfferRecyclerView()
        callOfferDeatils()
        hideCartView()
    }


    override fun callOfferDeatils() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callOfferDetailsApi(_id)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)

        }
    }

    override fun setOfferDetailsResp(res: OrderDetailsResp) {
        if (Validation.isValidStatus(res) && Validation.isValidObject(res) && Validation.isValidArrayList(res.buyone_getone!!)) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            offerDetailsList = res
            if (Validation.isValidList(res.buyone_getone!![0].products)) {
                setupData()
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)

        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setupData() {
        if (Validation.isValidList(offerDetailsList.buyone_getone!![0].products))
            offersDetailsAdapter.addList(offerDetailsList.buyone_getone!![0].products!!)

        ImageLoader.setImage(image_product, offerDetailsList.buyone_getone!![0].pic!![0].pic!!)
        product_name.text = offerDetailsList.buyone_getone!![0].title
        tv_product_price.visibility = View.GONE
        tv_details.text = offerDetailsList.buyone_getone!![0].description

        var first = offerDetailsList.buyone_getone!![0].products!![0].selling_price!!.toInt()
        var second = offerDetailsList.buyone_getone!![0].products!![0].selling_price!!.toInt()
        tv_price_amount.text = CommonUtils.getRupeesSymbol(this.context,(first + second).toString())

        for (i in offerDetailsList.buyone_getone!![0].products!!) {
            if (i.is_buyone == "1") {
                tv_offers_amt.text = CommonUtils.getRupeesSymbol(this.context, i.selling_price!!)
            }
        }
    }

    private fun setupOfferRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        offersDetailsAdapter = BaseRecAdapter(context = this, type = R.layout.partial_offers_recyclerview_item, adapterClickListener = this, adapterType = Constants.TYPE_OFFERS_DETAILS_ADAPTER)
        rv_offers_detail.apply {
            layoutManager = linearLayout
            adapter = offersDetailsAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {

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
