package com.freshbasket.customer.screens.productdetail.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.ProductDetailsResp
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import kotlinx.android.synthetic.main.partial_product_details_list_item.view.*


class ProductDetailFragment : Fragment(), IAdapterClickListener {


    private var mActivity: FragmentActivity? = null
    private var mContext: Context? = null
    private var fragmentView: View? = null
    private lateinit var productDeatilsAdapter: BaseRecAdapter
    private lateinit var values: ArrayList<ProductDetailsResp.Product.Spec>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mContext = context
        mActivity = requireActivity()
        arguments.apply {
            values = this!!.getParcelableArrayList(Constants.PRODUCT_DETAIL_RESP)
        }
        fragmentView = inflater.inflate(R.layout.partial_product_details_list_item, container, false)
        return fragmentView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    fun init() {
        setupProductDeatilsFragmentRecyclerView()
    }

    private fun setupProductDeatilsFragmentRecyclerView() {
        val linearLayout = LinearLayoutManager(context)
        linearLayout.orientation = LinearLayoutManager.VERTICAL
        productDeatilsAdapter = BaseRecAdapter(context = context!!, type = R.layout.partial_information_list_item, adapterClickListener = this, adapterType = Constants.TYPE_PRODUCT_DETAILS_ADAPTER)
        fragmentView!!.rv_product_deatils.apply {
            layoutManager = linearLayout
            adapter = productDeatilsAdapter
            isNestedScrollingEnabled = false
        }
        productDeatilsAdapter.addList(values)
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {

    }


    companion object {

        @JvmStatic
        fun newInstance(subCatId: ArrayList<ProductDetailsResp.Product.Spec>): ProductDetailFragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList(Constants.PRODUCT_DETAIL_RESP, subCatId)
            val fragment = ProductDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}
