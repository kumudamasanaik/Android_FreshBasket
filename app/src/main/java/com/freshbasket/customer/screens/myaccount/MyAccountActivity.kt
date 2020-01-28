package com.freshbasket.customer.screens.myaccount

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.Customer
import com.freshbasket.customer.model.MyAccountResp
import com.freshbasket.customer.screens.addressselection.SelectAddressActivity
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.changepassword.ChangePasswordActivity
import com.freshbasket.customer.screens.myorder.MyOrderActivity
import com.freshbasket.customer.screens.mywallet.MyWalletActivity
import com.freshbasket.customer.screens.notification.NotificationActivity
import com.freshbasket.customer.screens.referearn.ReferEarnActivity
import com.freshbasket.customer.util.*
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_my_account.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*


class MyAccountActivity : SubBaseActivity(), View.OnClickListener, IAdapterClickListener, MyAccountActivityContract.View {
    private val TAG = "MyAccountActivity"
    private var mContext: Context? = null
    private lateinit var source: String
    private lateinit var bitmap: Bitmap
    private lateinit var myAccountResp: MyAccountResp
    private var cur_User: Customer? = null

    private lateinit var snackbbar: View
    var resultImageString: String = ""
    private lateinit var presenter: MyAccountActivityPresenter
    private lateinit var customerPicURl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_my_account, fragment_layout)
        setToolBarTittle(getString(R.string.myProfile))
        mContext = this
        hideCartView()
        init()
    }

    override fun init() {
        presenter = MyAccountActivityPresenter(this)
        snackbbar = snack_barview
        empty_button.setOnClickListener { updateMyProfile() }
        error_button.setOnClickListener { updateMyProfile() }
        // tv_profile_name.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_edit), null)

        tv_profile_email.visibility = View.VISIBLE
        updateCustomerUI()

        tv_my_order_title.setOnClickListener(this)
        tv_my_wallet_title.setOnClickListener(this)
        tv_notify.setOnClickListener(this)
        tv_refer.setOnClickListener(this)
        tv_change_pass.setOnClickListener(this)
        tv_delivery_address.setOnClickListener(this)
        tv_logout.setOnClickListener(this)
        if (CommonUtils.getDeliveryAddress().isNotEmpty())
            tv_address_my_loc.text = CommonUtils.getDeliveryAddress()

        iv_user_profile_pic.setOnClickListener {
            CropImage.activity().start(this)
        }

        tv_profile_email.setOnClickListener {
            tv_profile_email.isCursorVisible = true
            tv_profile_email.inputType = InputType.TYPE_CLASS_TEXT
        }

        iv_edit_address_loc.setOnClickListener {
            val intent = Intent(context, SelectAddressActivity::class.java)
            intent.apply {
                putExtra(Constants.SOURCE, Constants.MY_ACCOUNT_SCREEN)
            }
            startActivity(intent)
        }


        iv_edit_profile.setOnClickListener {
            tv_profile_name.isCursorVisible = true
            tv_profile_name.setSelection(tv_profile_name.text.length)
            iv_edit_profile.visibility = View.VISIBLE

        }
        btn_update.setOnClickListener {
            if (!tv_profile_name.text.isNullOrEmpty() && !tv_profile_email.text.isNullOrEmpty()) {
                updateMyProfile()
                tv_profile_name.isCursorVisible = false
                tv_profile_email.isCursorVisible = false
            } else {
                toast(getString(R.string.err_please_enter_valid_name), Toast.LENGTH_LONG)
            }

        }

        updateCustomerProfilePic()

    }

    override fun updateMyProfile() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)

            presenter.updateProfile(tv_profile_name.text.toString(), tv_profile_email.text.toString(), resultImageString)
        } else {
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setMyAccountApiRes(res: MyAccountResp) {
        if (Validation.isValidStatus(res)) {
            if (res.result!!.isNotEmpty()) {
                showViewState(MultiStateView.VIEW_STATE_CONTENT)
                CommonUtils.setCustomerData(res.result[0])
                updateCustomerUI()
                hideSoftKeyboard()

            } else
                showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }


    override fun showMsg(msg: String?) {
        showSnackBar(snackbbar, msg ?: getString(R.string.Whoops_Something_went_wrong))
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


    private fun updateCustomerUI() {
        cur_User = CommonUtils.getCurrentUser()
        cur_User?.apply {

            if (email!!.isNotEmpty())
                tv_profile_email.setText(email)

            if (first_name!!.isNotEmpty())
                tv_profile_name.setText(first_name)

            if (pic!!.isNotEmpty())
                ImageLoader.setImage(iv_user_profile_pic, pic)

        }

    }

    private fun updateCustomerProfilePic() {

        if (CommonUtils.getProfilePic().isNotEmpty()) {
            iv_user_profile_pic.visibility = View.VISIBLE
            customerPicURl = CommonUtils.getProfilePic()
            if (Validation.isValidString(customerPicURl)) {
                if (customerPicURl.startsWith("https://graph.facebook.com") || customerPicURl.startsWith("https://lh6.googleusercontent.com")) {
                    ImageLoader.loadImagesWithoutBaseUrl(imageView = iv_user_profile_pic, imageUrl = customerPicURl)
                } else {
                    ImageLoader.setImage(imageView = iv_user_profile_pic, imageUrl = customerPicURl)
                }
            }
        } else {
            iv_user_profile_pic.setBackgroundResource(R.drawable.ic_dummy_profile)
        }
    }


    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.tv_my_order_title -> {
                startActivity(Intent(this, MyOrderActivity::class.java))
            }
            R.id.tv_my_wallet_title -> {
                startActivity(Intent(this, MyWalletActivity::class.java))
            }
            R.id.tv_notify -> {
                startActivity(Intent(this, NotificationActivity::class.java))
            }
            R.id.tv_refer -> {
                startActivity(Intent(this, ReferEarnActivity::class.java))
            }
            R.id.tv_change_pass -> {
                navigateToChangePsswrdScreen()
            }
            R.id.tv_delivery_address -> {
                //startActivity(Intent(this, DeliveryAddressActivity::class.java))
                navigateToAddAddressActivityScreen()
            }
            R.id.tv_logout -> {
                if (CommonUtils.getCustomerID().isNotEmpty()) {
                    CommonUtils.showDialog(mContext!!, getString(R.string.do_u_want_to_logout))
                } else {
                    CommonUtils.showCustomToast(mContext!!, getString(R.string.Please_login_to_the_application), 1)
                }

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                iv_user_profile_pic.setImageURI(resultUri)

                bitmap = (iv_user_profile_pic.drawable as BitmapDrawable).bitmap
                getBase64Image(bitmap).execute()

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class getBase64Image(private var bitmap: Bitmap) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg voids: Void): String {
            return CommonUtils.convertToBase64(bitmap)
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            resultImageString = s
            MyLogUtils.d(TAG, "IMAGE URL :$resultImageString")

        }
    }


    override fun onclick(item: Any, pos: Int, type: String?, op: String) {

    }

    fun navigateToChangePsswrdScreen() {
        val bundle = Bundle()
        bundle.putString(Constants.SOURCE, Constants.MY_ACCOUNT)
        CommonUtils.startActivity(this, ChangePasswordActivity::class.java, bundle, false)
    }

    private fun navigateToAddAddressActivityScreen() {
        if (CommonUtils.isUserLogin()) {
            val intent = Intent(context, SelectAddressActivity::class.java)
            intent.apply {
                putExtra(Constants.SOURCE, Constants.MY_ACCOUNT)
            }
            startActivity(intent)
        } else {
            CommonUtils.showCustomToast(this.context, getString(R.string.Please_login_to_the_application), 1)
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
}