package com.freshbasket.customer.screens.filtersactivity.refinedfragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.listner.OnFragmentInteractionListener
import com.freshbasket.customer.model.Refine
import com.freshbasket.customer.model.Sort
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.screens.commonadapter.FilterAdapter
import com.freshbasket.customer.screens.filtersactivity.FilterItemsActivity
import com.freshbasket.customer.util.Validation
import kotlinx.android.synthetic.main.partial_common_filters_fragment_item.*
import kotlinx.android.synthetic.main.partial_common_filters_fragment_item.view.*
import java.util.*


class RefinedFragment : Fragment(), IAdapterClickListener, View.OnClickListener {

    private var mContext: Context? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var mFilterHeaderAdapter: BaseRecAdapter
    private lateinit var mFilterValuesAdapter: FilterAdapter
    private lateinit var fragmentView: View
    internal lateinit var refineArrayList: ArrayList<Refine>
    internal lateinit var refinevaluesArrayList: ArrayList<Sort>
    private lateinit var filterType: String
    private var filtervalue: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_refined_screen, container, false)
        mContext = context
        init()
        return fragmentView

    }


    private fun init() {
        setRecyclerHeader()
        setupRecyclerValues()
        setData()

        fragmentView.btn_clear.setOnClickListener(this)
        fragmentView.btn_done.setOnClickListener(this)
    }

    private fun setRecyclerHeader() {
        val linearLayout = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        mFilterHeaderAdapter = BaseRecAdapter(context!!, R.layout.filter_refined_adapter_headers_list_item, adapterType = Constants.TYPE_FILTER_ADAPTER, adapterClickListener = this)
        fragmentView.rv_filter_left_header.apply {
            layoutManager = linearLayout
            adapter = mFilterHeaderAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupRecyclerValues() {
        val linearLayout = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        mFilterValuesAdapter = FilterAdapter(this.context!!, Constants.REFINE_BY, adapterClickListener = this)
        fragmentView.filter_values_recyclerview.apply {
            layoutManager = linearLayout
            adapter = mFilterValuesAdapter
            isNestedScrollingEnabled = false
        }

    }

    private fun setData() {
        if (context != null && activity != null && activity is FilterItemsActivity) {
            this.refineArrayList = (activity as FilterItemsActivity).refineArrayList
            if (Validation.isValidArrayList(refineArrayList)) {
                refinevaluesArrayList = ArrayList()
                refineArrayList[0].setSelected(true)
                filterType = refineArrayList[0].name
                refinevaluesArrayList.addAll(refineArrayList[0].refineValues)

                mFilterHeaderAdapter.addList(refineArrayList)
                mFilterValuesAdapter.addList(refinevaluesArrayList)
            } else {
                Toast.makeText(context, R.string.no_data_found, Toast.LENGTH_SHORT).show()

            }
        }


    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (Validation.isValidObject(item) && type == Constants.TYPE_RIFINED_LIST) {
            if (item is Refine) {
                for (refine in refineArrayList)
                    refine.setSelected(false)
                item.setSelected(true)
                mFilterHeaderAdapter.notifyDataSetChanged()
                filterType = item.name
                mFilterValuesAdapter.addList(item.refineValues)
            }
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btn_done -> {
                if (Validation.isValidObject(mFilterValuesAdapter) && Validation.isValidArrayList(mFilterValuesAdapter.list!!)) {
                    for (refineValue: Sort in mFilterValuesAdapter.list!!) {
                        if (refineValue.isChecked) {
                            startProductListScreen(mFilterValuesAdapter.list!!)
                            return
                        }
                    }
                    mFilterHeaderAdapter.notifyDataSetChanged()
                }
            }
            R.id.btn_clear -> {
                if (Validation.isValidObject(mFilterValuesAdapter) && Validation.isValidList(mFilterValuesAdapter.list!!)) {
                    for (filterValue in mFilterValuesAdapter.list!!) {
                        filterValue.setChecked(false)
                    }
                    mFilterValuesAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    fun startProductListScreen(itemList: ArrayList<Sort>) {
        if (Validation.isValidString(filterType)) {
            val filterValBuilder = StringBuilder()
            /* for (value in itemList) {
                 if (value.isChecked) {
                     if (filterType == Constants.OFFER)
                         filterValBuilder.append(value.getValue() + ",")
                     else
                         filterValBuilder.append(value.getName() + ",")
                 }
             }*/
            /* filtervalue = filterValBuilder.substring(0, filterValBuilder.length - 1)
             if (Validation.isValidString(filtervalue)) {
                 input = ProductListInput()
                 if (filterType == Constants.PRICE)
                     input.setPrice(filtervalue)
                 if (filterType == Constants.OFFER)
                     input.setOffer(filtervalue)
                 if (filterType == Constants.BRAND)
                     input.setBrand(filtervalue)
                 input.set_id(CommonUtils.getCustomerId(context))
                 input.set_session(CommonUtils.getSession(context))
                 input.setWh_pincode(CommonUtils.getPincode(context))

                 val intent = Intent(activity, ProductListActivity::class.java)
                 intent.putExtra(Constants.SOURCE, Constants.SOURCE_FILTER)
                 intent.putExtra(Constants.FILTER_OBJ, input)
                 startActivity(intent)
             }*/
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onResume() {
        super.onResume()
    }
}