package com.freshbasket.customer.screens.productlist.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.listner.OnFragmentInteractionListener
import com.freshbasket.customer.model.FetchCartResp
import com.freshbasket.customer.model.Product
import com.freshbasket.customer.model.ProductResp
import com.freshbasket.customer.model.WishListResponse
import com.freshbasket.customer.screens.commonadapter.ProductListAdapter
import com.freshbasket.customer.screens.productdetail.ProductDetailsActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.SharedPreferenceManager
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.product_list_fragment.*
import kotlinx.android.synthetic.main.product_list_fragment.view.*


class ProductListFragment : Fragment(), IAdapterClickListener, ProductListFragmentContract.View {


    private lateinit var productListPresenter: ProductListFragmentPresenter
    private var listener: OnFragmentInteractionListener? = null
    private var mActivity: FragmentActivity? = null
    private var mContext: Context? = null
    private var fragmentView: View? = null
    private lateinit var productListAdapter: ProductListAdapter
    private var productList: ArrayList<Product>? = null
    private var subCatId: String? = null
    private var modifiedProd: Product? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mContext = context
        mActivity = requireActivity()
        fragmentView= inflater.inflate(R.layout.product_list_fragment, container, false)
        return fragmentView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    override fun init() {
        productListPresenter = ProductListFragmentPresenter(this)

        error_button.setOnClickListener { callProductListApi() }
        empty_button.setOnClickListener { callProductListApi() }

        setupProductListFragmentRecyclerView()
    }

    private fun setupProductListFragmentRecyclerView() {
        val linearLayout = LinearLayoutManager(context)
        linearLayout.orientation = LinearLayoutManager.VERTICAL
        productListAdapter = ProductListAdapter(context = context!!, adapterClickListener = this, adapterType = Constants.TYPE_PRODUCT_LIST_ADAPTER)
        fragmentView!!.rv_productlist.apply {
            layoutManager = linearLayout
            adapter = productListAdapter
            isNestedScrollingEnabled = false
        }

    }

    override fun callProductListApi() {
        if (NetworkStatus.isOnline2(mContext)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            productListPresenter.callProductListApi(subCatId!!)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setProductListApiRes(productlistRes: ProductResp) {
        if (productlistRes.isSuccess()) {
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            productlistRes.product?.apply {
                if (productlistRes.product.isNotEmpty()) {
                    productListAdapter.addList(productlistRes.product)
                } else {
                    error_msg.text=getString(R.string.no_product_found)
                    showViewState(MultiStateView.VIEW_STATE_EMPTY)
                }
            }
        } else {
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }


    override fun modifycartApiRes(productlistRes: FetchCartResp?) {
        hideLoader()
        if (productlistRes!!.isSuccess()) {
            SharedPreferenceManager.saveCartData(productlistRes.summary)
            productListAdapter.showModifiedRes(Constants.RES_SUCCESS)

        } else
            productListAdapter.showModifiedRes(Constants.RES_FAILED)

    }

    override fun setWishListApiRes(wishlistRes: WishListResponse) {
        hideLoader()
        if (wishlistRes.isSuccess()) {
            productListAdapter.showModifiedWishListRes(Constants.RES_SUCCESS)
        } else
            productListAdapter.showModifiedWishListRes(Constants.RES_FAILED)


    }

    override fun showMsg(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showLoader() {
        CommonUtils.showLoading(this.mContext!!, true)

    }

    override fun hideLoader() {
        CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {
        if (multistateview != null)
            multistateview.viewState = state
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {

        @JvmStatic
        fun newInstance(subCatId: Int): ProductListFragment {
            val bundle = Bundle()
            bundle.putInt(Constants.SUBCAT_ID, subCatId)
            val fragment = ProductListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {

        if (item is Product && item.selSku?._id != null) {
            modifiedProd = item
            when (op) {
                Constants.OP_PROD_DETAILS -> {
                    if (NetworkStatus.isOnline2(context)) {
                        val intent = Intent(mContext, ProductDetailsActivity::class.java)
                        intent.putExtra(Constants.KEY_PROD_ID, item.selSku?._id!!)
                        startActivity(intent)
                    } else
                        showMsg(getString(R.string.error_no_internet))


                }

                Constants.OP_MODIFY_CART -> {
                    if (NetworkStatus.isOnline2(context)) {
                        showLoader()
                        productListPresenter.modifyCart(item.modifyCartIp!!)
                    } else
                        showMsg(getString(R.string.error_no_internet))
                }
                Constants.WISHLIST -> {
                    if (NetworkStatus.isOnline2(context)) {
                        showLoader()
                        productListPresenter.modifyWishList(if (modifiedProd?.wishlist == 0) Constants.WISHLIST_CREATE else Constants.WISHLIST_DELETE, item._id!!)

                    } else
                        showMsg(getString(R.string.error_no_internet))
                }

                /* Constants.OP_REMOVE_FROM_CART -> productListPresenter.removeFromCart(item.modifyCartIp!!)*/
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (subCatId != null)
            callProductListApi()
    }

    fun setupChildData(childId: String?) {
        if (NetworkStatus.isOnline2(context)) {
            subCatId = childId
            callProductListApi()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }
}
