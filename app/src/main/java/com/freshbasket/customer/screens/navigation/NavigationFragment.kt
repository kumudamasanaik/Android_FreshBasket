package com.freshbasket.customer.screens.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.NavigationDrawerModel
import com.freshbasket.customer.screens.addressselection.SelectAddressActivity
import com.freshbasket.customer.screens.cart.CartActivity
import com.freshbasket.customer.screens.changepassword.ChangePasswordActivity
import com.freshbasket.customer.screens.combo.ComboActivity
import com.freshbasket.customer.screens.customerservices.CustomerServicesActivity
import com.freshbasket.customer.screens.faq.FaqActivity
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.screens.login.LoginActivity
import com.freshbasket.customer.screens.myaccount.MyAccountActivity
import com.freshbasket.customer.screens.myorder.MyOrderActivity
import com.freshbasket.customer.screens.notification.NotificationActivity
import com.freshbasket.customer.screens.offers.OffersActivity
import com.freshbasket.customer.screens.paymentstatus.PaymentStatusActivity
import com.freshbasket.customer.screens.referearn.ReferEarnActivity
import com.freshbasket.customer.screens.shopbycategory.ShopByCategoryActivity
import com.freshbasket.customer.screens.smartbasket.SmartBasketActivity
import com.freshbasket.customer.screens.specialshop.SpecialShopActivity
import com.freshbasket.customer.screens.wishlist.WishListActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.ImageLoader
import com.freshbasket.customer.util.Validation
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.fragment_navigation.*


class NavigationFragment : Fragment(), IAdapterClickListener {

    lateinit var navListAdapter: NavigationDrawerListAdapter
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    lateinit var mActivity: FragmentActivity
    lateinit var mContext: Context
    lateinit var list: ArrayList<NavigationDrawerModel>
    private lateinit var customerPicURl: String
    private var mListener: OnFragmentInteractionListener? = null
    private var mDrawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }


    override fun onStart() {
        super.onStart()

        if (CommonUtils.isUserLogin()) {
            location_view.visibility = View.VISIBLE
            if (CommonUtils.getDeliveryAddress().isNotEmpty())
                tv_address_loc_nav.text = CommonUtils.getDeliveryAddress()
            else
                tv_address_loc_nav.text = getString(R.string.dummy_address)
        } else
            location_view.visibility = View.GONE


        rl_nav_head.setOnClickListener { }

        setUpNavigationList()
        upadateCustomerName()
        updateProfilePic()
    }

    private fun updateProfilePic() {

        if (CommonUtils.getProfilePic().isNotEmpty()) {
            iv_user_pic.visibility = View.VISIBLE
            customerPicURl = CommonUtils.getProfilePic()
            if (Validation.isValidString(customerPicURl)) {
                if (customerPicURl.startsWith("https://graph.facebook.com") || customerPicURl.startsWith("https://lh6.googleusercontent.com")) {
                    ImageLoader.loadImagesWithoutBaseUrl(imageView = iv_user_pic, imageUrl = customerPicURl)
                } else
                    ImageLoader.setImage(imageView = iv_user_pic, imageUrl = customerPicURl)

            }
        } else {
            iv_user_pic.setBackgroundResource(R.drawable.ic_dummy_profile)
        }
    }

    private fun upadateCustomerName() {
        if (CommonUtils.getCustomerFirstName().isNotEmpty()) {

            btn_login.visibility = View.GONE
            customer_name.visibility = View.VISIBLE
            customer_name.text = CommonUtils.getCustomerFirstName()
        } else {
            customer_name.visibility = View.GONE
            btn_login.visibility = View.VISIBLE
        }

        btn_login.setOnClickListener {
            NavigateLoginScreen()

        }

    }

    private fun NavigateLoginScreen() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.NAVIGATION_FRAGMENT)
        }
        startActivity(intent)


    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = requireActivity()
        /*if (mContext is OnFragmentInteractionListener) {
            listener = mContext
        } else {
            throw RuntimeException(mContext.toString() + " must implement OnFragmentInteractionListener")
        }*/
    }

    fun setUpNavigationList() {
        list = ArrayList()
        val titles = mActivity.resources.getStringArray(R.array.nav_title_list)
        val icons = resources.obtainTypedArray(R.array.nav_icon_list)
        val accItem = mActivity.resources.getStringArray(R.array.my_account)


        for ((index, value) in titles.withIndex()) {
            val navModel = NavigationDrawerModel()
            navModel.title = value
            navModel.id = index

            if (index < icons.length())
                navModel.icon = ContextCompat.getDrawable(mContext, icons.getResourceId(index, -1))

            if (navModel.title!!.contentEquals(mActivity.resources.getString(R.string.my_account))) {
                if (CommonUtils.getCustomerID().isNotEmpty())
                    navModel.subItemList = accItem
            }


            list.add(navModel)
        }

        navListAdapter = NavigationDrawerListAdapter(context = mContext)
        nav_listview.setAdapter(navListAdapter)
        navListAdapter.addArrayList(list)

        icons.recycle()

        setListViewListener()
    }


    private fun setListViewListener() {
        var previousGroup = -1
        nav_listview.setOnGroupExpandListener {
            if (it != previousGroup)
                nav_listview.collapseGroup(previousGroup)
            previousGroup = it
        }



        nav_listview.setOnGroupClickListener { parent, v, groupPosition, id ->
            if (parent.isGroupExpanded(groupPosition)) {
                parent.collapseGroup(groupPosition)
            } else {
                parent.expandGroup(groupPosition)
            }
            when (list[groupPosition].title) {

                getString(R.string.home) -> {
                    closeDrawer()
                    navigateToHomeScreen()
                }

                getString(R.string.refer_earn) -> {
                    closeDrawer()
                    if (!CommonUtils.isUserLogin()) {
                        CommonUtils.showCustomToast(mContext, getString(R.string.Please_login_to_the_application), 1)
                    } else
                        startActivity(Intent(mContext, ReferEarnActivity::class.java))

                }

                getString(R.string.smart_basket) -> {
                    closeDrawer()
                    startActivity(Intent(mContext, ComboActivity::class.java))
                }

                getString(R.string.my_account) -> {
                    closeDrawer()
                    if (!CommonUtils.isUserLogin())
                        CommonUtils.showCustomToast(mContext, getString(R.string.Please_login_to_the_application), 1)
                }


                getString(R.string.shop_by_category) -> {
                    closeDrawer()
                    navigateShopByCategoryScreen()
                }


                getString(R.string.offers) -> {
                    closeDrawer()
                    startActivity(Intent(mContext, OffersActivity::class.java))

                   // Toast.makeText(mContext, getString(R.string.need_to_implement), Toast.LENGTH_SHORT).show()
                }

                getString(R.string.special_shops) -> {
                    closeDrawer()
                    startActivity(Intent(mContext, SpecialShopActivity::class.java))
                }

                getString(R.string.wish_list) -> {
                    closeDrawer()
                    startActivity(Intent(mContext, WishListActivity::class.java))
                }

                getString(R.string.customer_services) -> {
                    closeDrawer()
                    startActivity(Intent(mContext, CustomerServicesActivity::class.java))
                    //Toast.makeText(mContext, getString(R.string.need_to_implement), Toast.LENGTH_SHORT).show()
                }

                getString(R.string.notifications) -> {
                    closeDrawer()
                    startActivity(Intent(mContext, NotificationActivity::class.java))
                }


                getString(R.string.faqs) -> {
                    closeDrawer()
                    startActivity(Intent(mContext, FaqActivity::class.java))
                }


            }
            true
        }

        nav_listview.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            when (list[groupPosition].subItemList!![childPosition]) {

                getString(R.string.my_orders) -> {

                    startActivity(Intent(mContext, MyOrderActivity::class.java))
                }

                getString(R.string.my_wallet) -> {
                    Toast.makeText(mContext, getString(R.string.need_to_implement), Toast.LENGTH_SHORT).show()

                    //  startActivity(Intent(mContext, MyWalletActivity::class.java))
                }


                getString(R.string.third_prty_payment) -> {

                    startActivity(Intent(mContext, PaymentStatusActivity::class.java))
                }


                getString(R.string.pay_now_fr_order) -> {

                    startActivity(Intent(mContext, PaymentStatusActivity::class.java))
                }


                getString(R.string.my_profile) -> {

                    startActivity(Intent(mContext, MyAccountActivity::class.java))
                }

                getString(R.string.change_pswrd) -> {
                    closeDrawer()
                    //startActivity(Intent(mContext, ChangePasswordActivity::class.java))
                //    if ()
                    navigateChangePasswordScreen()
                }


                getString(R.string.delivery_address) -> {

                    if (CommonUtils.isUserLogin()) {
                        navigateToDeliveryAddAddressScreen()
                    } else {
                        CommonUtils.showCustomToast(mContext, getString(R.string.Please_login_to_the_application), 1)
                    }
                }

                getString(R.string.logout) -> {

                    if (CommonUtils.getCustomerID().isNotEmpty()) {
                        CommonUtils.showDialog(mContext, getString(R.string.do_u_want_to_logout))
                    } else {
                        CommonUtils.showCustomToast(mContext, getString(R.string.Please_login_to_the_application), 1)
                    }
                }
            }
            true
        }
    }

    private fun navigateToHomeScreen() {

        val intent = Intent(context, HomeActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.NAVIGATION_FRAGMENT)
        }
        startActivity(intent)

    }


    override fun onclick(item: Any, pos: Int, type: String?, op: String) {

    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = NavigationFragment()
    }

    private fun navigateShopByCategoryScreen() {
        val intent = Intent(context, ShopByCategoryActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.NAVIGATION_FRAGMENT)
        }
        startActivity(intent)

    }

    private fun navigateChangePasswordScreen() {
        val intent = Intent(context, ChangePasswordActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.NAVIGATION_FRAGMENT)
        }
        startActivity(intent)

    }

    private fun navigateToDeliveryAddAddressScreen() {
        val intent = Intent(context, SelectAddressActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.NAVIGATION_FRAGMENT)
        }
        startActivity(intent)

    }

    fun setDrawer(drawerLayout: DrawerLayout) {
        mDrawerLayout = drawerLayout
    }


    fun closeDrawer() {

        if (mDrawerLayout != null)
            mDrawerLayout!!.closeDrawer(Gravity.START)
    }


}
