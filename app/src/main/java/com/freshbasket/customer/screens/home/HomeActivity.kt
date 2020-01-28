package com.freshbasket.customer.screens.home

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.Toast
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.customviews.MyTimerTask
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.*
import com.freshbasket.customer.screens.addressselection.SelectAddressActivity
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BannerAdapter
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.screens.location.LocationActivity
import com.freshbasket.customer.screens.productlist.ProductListActivity
import com.freshbasket.customer.screens.search.SearchActivity
import com.freshbasket.customer.screens.shopbycategory.ShopByCategoryActivity
import com.freshbasket.customer.util.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.partialy_searchview.*
import kotlinx.android.synthetic.main.tool_bar_with_back.*
import kotlinx.android.synthetic.main.view_pager_banner_layout.*
import java.io.IOException
import java.util.*


class HomeActivity : SubBaseActivity(), IAdapterClickListener, HomeContract.View, ViewPager.OnPageChangeListener {
    private val TAG = "HomeActivity"
    private var mContext: Context? = null
    private lateinit var source: String
    private lateinit var dealsProductsAdapter: BaseRecAdapter
    private lateinit var shopByMainCategoryAdapter: BaseRecAdapter
    private lateinit var bannerApter: BannerAdapter
    private lateinit var homePresenter: HomePresenter
    private lateinit var home: Home
    private lateinit var snackbbar: View
    private var doubleBackToExitPressedOnce = false
    private var isNetworkLocation: Boolean = false
    private var isGPSLocation: Boolean = false
    private var address: Address? = null
    private var handler: Handler? = null
    private var bannerrunnable: Runnable? = null
    private var mainBannerCurrentPage = 0
    private  var firebaseToken: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_home, fragment_layout)
        setToolBarTittle(getString(R.string.home))
        mContext = this@HomeActivity
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            MyLogUtils.d(TAG, source)
        }
        showMenu()
        showSearchView()
        init()
    }

    override fun init() {
        homePresenter = HomePresenter(this)
        hideSoftKeyboard()
        setupBannerViewHolder()
        setupShopByCategory()
        setupDealsProduct()
        snackbbar = snack_barview

        empty_button.setOnClickListener {
            callFireBaseTokenToServer()
            callHomeBoardApi()
        }
        error_button.setOnClickListener {
            callFireBaseTokenToServer()
            callHomeBoardApi()
        }

        /*USER LOGIN RECIEVE THE NOTIFICATION*/
        if(CommonUtils.isUserLogin())
            getFirebaseToken()

        //callHomeBoardApi()

        iv_edit.setOnClickListener {
            if (CommonUtils.isUserLogin()) {
                val intent = Intent(context, SelectAddressActivity::class.java)
                intent.apply {
                    putExtra(Constants.SOURCE, Constants.HOME_SCREEN)
                }
                startActivity(intent)
            } else {
                CommonUtils.showCustomToast(this.context, getString(R.string.Please_login_to_the_application), 1)
            }
        }

        tv_shop_by_cat_view_all.setOnClickListener {
            navigateShopByCategoryScreen()
        }
        tv_deals_view_all.setOnClickListener {

        }

        layout_search_.setOnClickListener {
            //Toast.makeText(this, getString(R.string.need_to_implement), Toast.LENGTH_SHORT).show()
           CommonUtils.startActivity(this, SearchActivity::class.java, Bundle(), false)
           // CommonUtils.startActivity(this, FilterActivity::class.java, Bundle(), false)
        }

        layout_shop_by_category.setOnClickListener {
            val intent = Intent(context, ShopByCategoryActivity::class.java)
            intent.apply {
                putExtra(Constants.SOURCE, Constants.HOME_SCREEN)
            }
            startActivity(intent)
        }

        setupLocation()

        tv_screen_title.setOnClickListener {
            navigateLocationScreen()
        }
    }

    private fun getFirebaseToken() {
        Thread(Runnable {
            try {
                firebaseToken = FirebaseInstanceId.getInstance().getToken(getString(R.string.firebase_sender_id), "FCM")
                MyLogUtils.d(TAG, "FIREBASE TOKEN :$firebaseToken")
            } catch (e: IOException) {
                e.printStackTrace()
            }

            this@HomeActivity.runOnUiThread {
                if (!firebaseToken.isNullOrEmpty()) {
                    CommonUtils.saveFirebaseToken(firebaseToken!!)
                    callFireBaseTokenToServer()
                }
            }
        }).start()
    }

    private fun setupViewPagerAutoRotate() {
        handler = Handler()
        Timer().schedule(MyTimerTask(handler!!, MyRunnableThread()), 1000, 1000)
        main_banner_pager.addOnPageChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        callHomeBoardApi()
        if (Validation.isValidObject(CommonUtils.getMyAddress())) {
            address = CommonUtils.getMyAddress()
            tv_screen_title.text = "My Location" + "\n" + address!!.getAddressLine(0)
            tv_screen_title.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(context, R.drawable.ic_edit), null)
        }
    }

    private fun setupLocation() {
        val mListener = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (true) {
            isGPSLocation = mListener.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkLocation = mListener.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
    }

    override fun showMsg(msg: String?) {
        showSnackBar(snackbbar, msg!!)
        //  showToastMsg(msg!!)
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

    override fun callHomeBoardApi() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            homePresenter.callDashBoardApi()

        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun callFireBaseTokenToServer() {
        if (NetworkStatus.isOnline2(this) && CommonUtils.getFireBaseToken().length > 0) {
           // showViewState(MultiStateView.VIEW_STATE_LOADING)
            homePresenter.callFireBaseTokenToServer()
        } else {
            showMsg(getString(R.string.error_no_internet))
        }
    }

    override fun setFireBaseTokenToApiResp(responce: CommonRes) {
       /* if (Validation.isValidStatus(responce))
            showMsg(responce.message)*/
    }

    override fun setHomeApiResp(homeResp: HomeResp) {
        if (Validation.isValidStatus(homeResp) && Validation.isValidObject(homeResp.result)) {
            home = homeResp.result
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
            SharedPreferenceManager.saveCartData(homeResp.summary)
            setData()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {

        home.category?.apply {
            SharedPreferenceManager.saveCategoryListData(home.category!!)
        }

        if (!home.customercare_email.isNullOrEmpty() && !home.customercare_mobile.isNullOrEmpty())
            CommonUtils.setCustomerEmailMobileNumber(home.customercare_email!!, home.customercare_mobile!!)

        if (!home.share_message.isNullOrEmpty() && !home.refferal_code.isNullOrEmpty())
            CommonUtils.setShareMessage(home.share_message!!, home.refferal_code!!)

        if (home.banner!!.isNotEmpty())
            bannerApter.addList(home.banner!!)


        if (home.deals!!.isNotEmpty()) {
            layout_deals.visibility = View.VISIBLE
            dealsProductsAdapter.addList(home.deals!!)
        } else
            layout_deals.visibility = View.GONE


        home.category?.apply {
            if (home.category!!.isNotEmpty()) {
                layout_shop_by_cat.visibility = View.VISIBLE
                shopByMainCategoryAdapter.addList(home.category!!)
            } else
                layout_shop_by_cat.visibility = View.GONE
        }
    }

    private fun setupBannerViewHolder() {
        bannerApter = BannerAdapter(this, R.layout.item_banner_list_item)
        main_banner_pager.adapter = bannerApter
        main_banner_indicator.setViewPager(main_banner_pager)
        setupViewPagerAutoRotate()
    }

    private fun setupShopByCategory() {
        val gridLayoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        shopByMainCategoryAdapter = BaseRecAdapter(this, R.layout.item_main_category, adapterType = Constants.TYPE_MAIN_CAT_ADAPTER, adapterClickListener = this)
        val layoutManager = gridLayoutManager
        rv_category_products.apply {
            setLayoutManager(layoutManager)
            //  addItemDecoration(GridDividerDecoration(context))
            adapter = shopByMainCategoryAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupDealsProduct() {
        val gridLayoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        dealsProductsAdapter = BaseRecAdapter(this, R.layout.item_main_category, adapterType = Constants.TYPE_MAIN_CAT_ADAPTER, adapterClickListener = this)
        val layoutManager = gridLayoutManager
        rv_deals_product.apply {
            setLayoutManager(layoutManager)
            // addItemDecoration(GridDividerDecoration(context))
            adapter = dealsProductsAdapter
            isNestedScrollingEnabled = false
        }
    }


    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (item is Category && type!!.contentEquals(Constants.TYPE_MAIN_CAT_ADAPTER)) {
            val intent = Intent(context, ProductListActivity::class.java)
            intent.apply {
                putExtra(Constants.SOURCE, Constants.SOURCE_SIGN)
                intent.putExtra(Constants.KEY_CAT_DATA, item)
                intent.putExtra(Constants.PARENT_CAT_ID, item._id)
                intent.putExtra(Constants.PARENT_CAT_NAME, item.name)
                intent.putExtra(Constants.POS, 0)
            }
            startActivity(intent)
        }
        if (item is Deal && type!!.contentEquals(Constants.TYPE_MAIN_CAT_ADAPTER)) {

        }
    }

    private fun navigateShopByCategoryScreen() {
        val intent = Intent(context, ShopByCategoryActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.HOME_SCREEN)
        }
        startActivity(intent)
    }

    private fun navigateLocationScreen() {
        if (isGPSLocation) {
            val intent = Intent(context, LocationActivity::class.java)
            intent.apply {
                putExtra(Constants.SOURCE, Constants.HOME_SCREEN)
                putExtra("provider", LocationManager.GPS_PROVIDER)
            }
            startActivity(intent)
            finish()

        } else if (isNetworkLocation) {
            val intent = Intent(context, LocationActivity::class.java)
            intent.apply {
                putExtra(Constants.SOURCE, Constants.HOME_SCREEN)
                putExtra("provider", LocationManager.NETWORK_PROVIDER)
            }
            startActivity(intent)
            finish()

        } else {
            //Device location is not set
            PermissionUtils.LocationSettingDialog.newInstance().show(supportFragmentManager, "Setting")
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        // showSnackBar(snackbbar,getString(R.string.back_exit_confirmation))
        Toast.makeText(this, getString(R.string.back_exit_confirmation), Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null && bannerrunnable != null) {
            handler!!.removeCallbacks(bannerrunnable)
            bannerrunnable = null
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        mainBannerCurrentPage = position
    }

    inner class MyRunnableThread : Runnable {
        override fun run() {
            if (Validation.isValidObject(main_banner_pager)) {
                if (mainBannerCurrentPage == bannerApter.getCount()) {
                    mainBannerCurrentPage = 0
                }
                main_banner_pager.setCurrentItem(mainBannerCurrentPage++, true)
            }
        }
    }
}