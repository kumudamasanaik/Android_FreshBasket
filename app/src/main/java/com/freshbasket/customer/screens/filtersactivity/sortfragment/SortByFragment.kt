package com.freshbasket.customer.screens.filtersactivity.sortfragment

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
import com.freshbasket.customer.model.Sort
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.screens.commonadapter.FilterAdapter
import com.freshbasket.customer.screens.filtersactivity.FilterItemsActivity
import com.freshbasket.customer.util.Validation
import kotlinx.android.synthetic.main.partial_common_filters_fragment_item.view.*
import java.util.*


class SortByFragment : Fragment(), IAdapterClickListener {

    private lateinit var fragmentView: View
    private var mContext: Context? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var mFilterHeaderAdapter: BaseRecAdapter
    private lateinit var mFilterValuesAdapter: FilterAdapter
    internal lateinit var sortArrayList: ArrayList<Sort>
    private lateinit var filterType: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_sort_by_screen, container, false)
        mContext = context
        init()
        return fragmentView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        fragmentView.rv_filter_left_header.visibility = View.GONE
        fragmentView.btn_clear.visibility = View.GONE
        setupRecyclerValues()
        setData()
    }


    private fun setupRecyclerValues() {
        val linearLayout = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        mFilterValuesAdapter = FilterAdapter(this.mContext!!, Constants.SORT, adapterClickListener = this)
        fragmentView.filter_values_recyclerview.apply {
            layoutManager = linearLayout
            adapter = mFilterValuesAdapter
            isNestedScrollingEnabled = false
        }
    }


    private fun setData() {
        if (context != null && activity != null && activity is FilterItemsActivity) {
            this.sortArrayList = (activity as FilterItemsActivity).sortArrayList
            if (Validation.isValidArrayList(sortArrayList)) {
                filterType = Constants.SORT
                mFilterValuesAdapter.addList(sortArrayList)
            } else {
                Toast.makeText(context, R.string.no_data_found, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (Validation.isValidObject(item) && type == Constants.SORT) {
            for (sortValue: Sort in sortArrayList)
                if (item.hashCode() != sortValue.hashCode()) {
                    sortValue.setChecked(false)
                }
            mFilterValuesAdapter.notifyDataSetChanged()
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