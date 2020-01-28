package com.freshbasket.customer.screens.faq

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
import com.freshbasket.customer.model.Faq
import com.freshbasket.customer.model.FaqRes
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showSnackBar
import kotlinx.android.synthetic.main.activity_faq.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*


class FaqActivity : SubBaseActivity(), IAdapterClickListener, FaqContract.View {

    private lateinit var snackbbar: View
    lateinit var faqAdapter: BaseRecAdapter
    private var mContext: Context? = null
    private var faqList: ArrayList<Faq>? = null
    private lateinit var questionsList: FaqRes
    private lateinit var presenter: FaqPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_faq, fragment_layout)
        setToolBarTittle(getString(R.string.faq))

        init()
    }

    override fun init() {
        mContext = this
        setupFaqRecyclerView()
        hideCartView()
        presenter = FaqPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { callFaq() }
        error_button.setOnClickListener { callFaq() }

        callFaq()
    }

    override fun callFaq() {

        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callFAQApi(Constants.ALL, Constants.FAQ)

        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)

        }
    }


    private fun setupFaqRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        faqAdapter = BaseRecAdapter(this, R.layout.items_faq_details, adapterType = Constants.TYPE_FAQ_ADAPTER, adapterClickListener = this)
        val layoutManager = linearLayout
        rv_faq.apply {
            setLayoutManager(layoutManager)
//            addItemDecoration(InsetDecoration(context))
            adapter = faqAdapter
            isNestedScrollingEnabled = false
        }

    }


    override fun setFaqResp(res: FaqRes) {
        if (Validation.isValidStatus(res)) {
            if (res.result!!.isNotEmpty()) {
                faqList = res.result
                showViewState(MultiStateView.VIEW_STATE_CONTENT)
                setData()
            } else
                showViewState(MultiStateView.VIEW_STATE_EMPTY)
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {
        setupListofQuestions()
    }

    private fun setupListofQuestions() {
        faqAdapter = BaseRecAdapter(mContext!!, R.layout.items_faq_details, adapterType = Constants.TYPE_ADDRESS_LIST_ADAPTER, adapterClickListener = this)
        rv_faq.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_faq.adapter = faqAdapter
        rv_faq.setHasFixedSize(true)
        if (faqList!!.isNotEmpty()) {
            faqAdapter.addList(faqList!!)
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


    override fun onclick(item: Any, pos: Int, type: String?, op: String) {

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
