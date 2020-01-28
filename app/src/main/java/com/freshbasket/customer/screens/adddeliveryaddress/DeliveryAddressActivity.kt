package com.freshbasket.customer.screens.adddeliveryaddress

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.customviews.MultiStateView
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.model.DeliveryAddRes
import com.freshbasket.customer.model.Locality
import com.freshbasket.customer.model.LocalityRes
import com.freshbasket.customer.screens.addressselection.SelectAddressActivity
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.commonadapter.BaseRecAdapter
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.MyLogUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showToastMsg
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.items_add_delivery_address.*
import kotlinx.android.synthetic.main.main_locality_dialog.view.*

class DeliveryAddressActivity : SubBaseActivity(), DeliveryAddressContract.View, View.OnClickListener, IAdapterClickListener {
    private lateinit var mContext: Context
    lateinit var presenter: DeliveryAddressPresenter
    private val TAG = "DeliveryAddressActivity"
    private var deliveryAddressInput: com.freshbasket.customer.model.inputmodel.Address? = null
    private var selected_id: Int? = 1
    private lateinit var addressList: ArrayList<Locality>
    private lateinit var myAddressListAdapter: BaseRecAdapter
    lateinit var dialog: Dialog
    private lateinit var source: String
    private var lat: String? = null
    private var lon: String? = null
    private var address_id: String? = null
    val result = intent
    private lateinit var snackbbar: View
    private var address: Address? = null
    private var sourceType: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.items_add_delivery_address, fragment_layout)
        setToolBarTittle(getString(R.string.addAddress))
        hideCartView()
        mContext = this
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
                sourceType = intent.getStringExtra(Constants.SOURCE_TYPE) ?: ""
            deliveryAddressInput = intent.getParcelableExtra(Constants.UPDATE_ADDRESS_DATA)
            MyLogUtils.d(TAG, source)
        }
        init()
    }

    override fun init() {
        getIntentData()
        tv_save.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.ic_right_arrow), null)
        et_locality.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(this, R.drawable.down_arrow_gray), null)

        presenter = DeliveryAddressPresenter(this)

        snackbbar = snack_barview
        empty_button.setOnClickListener { getlocality() }
        error_button.setOnClickListener { getlocality() }
        et_locality.setOnClickListener(this)
        tv_save.setOnClickListener(this)
        check_address_box.setOnClickListener(this)
        getlocality()

    }

    override fun getIntentData() {
        intent?.extras?.apply {
            if (source.isNotEmpty() && source.contentEquals(Constants.SOURCE_LOCATION_ACTIVITY)) {
                address = getParcelable(Constants.ADDRESS_LOC)
                source = getString(Constants.SOURCE)
                sourceType = getString(Constants.SOURCE_TYPE) ?: ""
                setUpAddressData()
                return
            } else {
                address = getParcelable(Constants.ADDRESS_LOC)
                source = getString(Constants.SOURCE)
                deliveryAddressInput = getParcelable(Constants.UPDATE_ADDRESS_DATA)
                deliveryAddressInput.apply { setupUI() }
                return
            }
        }
        finish()
    }

    override fun setUpAddressData() {
        CommonUtils.saveCurrentLocation(address)
        address?.apply {

            val stringBuilder = StringBuilder() as StringBuilder
            if (Validation.isValidString(featureName))
                et_house_num.setText(featureName)

            if (Validation.isValidString(postalCode))
                et_pinCode.setText(postalCode)

            if (Validation.isValidString(thoroughfare))
                stringBuilder.append(thoroughfare).append("," + " ")

            if (Validation.isValidString(subLocality))
                stringBuilder.append(subLocality).append("," + " ")

            if (Validation.isValidString(locality))
                stringBuilder.append(locality).append("," + " ")

            if (Validation.isValidString(adminArea))
                stringBuilder.append(adminArea).append("  ")

            et_area.setText(stringBuilder.toString())

            if (Validation.isValidString(latitude.toString()))
                lat = latitude.toString()

            if (Validation.isValidString(longitude.toString()))
                lon = longitude.toString()
        }
    }

    private fun setupUI() {
        if (deliveryAddressInput != null) {
            setupEditAddressUI()
        }
    }

    private fun setupEditAddressUI() {
        deliveryAddressInput?.apply {
            et_nick_name.setText(address_nickname)
            et_first_name.setText(first_name)
            et_last_name.setText(last_name)
            et_mobile_number.setText(phone)
            et_locality.text = locality
            et_house_num.setText(house_no)
            et_residential.setText(residential_complex)
            et_area.setText(area)
            et_pinCode.setText(pincode)
            et_street_name.setText(street_name)
            et_landmark.setText(landmark)
            address_id = _id
        }
    }

    override fun addDeliveryAddress() {
        deliveryAddressInput = com.freshbasket.customer.model.inputmodel.Address(
                address_nickname = et_nick_name.text.toString(),
                area = et_area.text.toString(),
                first_name = et_first_name.text.toString(),
                house_no = et_house_num.text.toString(),
                id_customer = CommonUtils.getCustomerID(),
                landmark = et_landmark.text.toString(),
                last_name = et_last_name.text.toString(),
                locality = et_locality.text.toString(),
                pincode = et_pinCode.text.toString(),
                residential_complex = et_residential.text.toString(),
                selected = selected_id,
                street_name = et_street_name.text.toString(),
                op = Constants.CREATE,
                lon = address!!.longitude.toString(),
                lat = address!!.latitude.toString(),
                phone = et_mobile_number.text.toString()
        )


        if (presenter.validate(deliveryAddressInput!!)) {
            if (NetworkStatus.isOnline2(this)) {
                showViewState(MultiStateView.VIEW_STATE_LOADING)
                presenter.callAddDeliveryAddress(deliveryAddressInput!!)
            } else {
                showToastMsg(getString(R.string.error_no_internet))
                showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }
    }

    override fun callUpdateAddress() {
        deliveryAddressInput = com.freshbasket.customer.model.inputmodel.Address(
                id_address = address_id!!,
                address_nickname = et_nick_name.text.toString(),
                area = et_area.text.toString(),
                first_name = et_first_name.text.toString(),
                house_no = et_house_num.text.toString(),
                id_customer = CommonUtils.getCustomerID(),
                landmark = et_landmark.text.toString(),
                last_name = et_last_name.text.toString(),
                locality = et_locality.text.toString(),
                pincode = et_pinCode.text.toString(),
                residential_complex = et_residential.text.toString(),
                selected = selected_id,
                street_name = et_street_name.text.toString(),
                op = Constants.UPDATE,
                lon = if (lon != null) lon else deliveryAddressInput!!.lon,
                lat = if (lat != null) lat else deliveryAddressInput!!.lat,
                phone = et_mobile_number.text.toString()
        )

        if (presenter.validate(deliveryAddressInput!!)) {
            if (NetworkStatus.isOnline2(this)) {
                showViewState(MultiStateView.VIEW_STATE_LOADING)
                deliveryAddressInput?._id = null
                presenter.callUpdateAddressApi(deliveryAddressInput!!)
            } else {
                showToastMsg(getString(R.string.error_no_internet))
                showViewState(MultiStateView.VIEW_STATE_ERROR)
            }
        }
    }

    override fun setAddDeliveryAddressRes(res: DeliveryAddRes) {
        if (Validation.isValidStatus(res)) {
            CommonUtils.showCustomToast(this.context, getString(R.string.address_added_successfully), 1)
            val intent = Intent(MyApplication.context, SelectAddressActivity::class.java)
            intent.apply {
                if (sourceType == Constants.ORDER_TYPE)
                    putExtra(Constants.SOURCE, Constants.ORDER_TYPE)
                else
                    putExtra(Constants.SOURCE, Constants.HOME_SCREEN)
                startActivity(intent)
            }
            finish()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    override fun setupdateAddressApiRes(res: DeliveryAddRes) {
        if (Validation.isValidStatus(res)) {
            CommonUtils.showCustomToast(this.context, getString(R.string.address_updated_successfully), 1)
            val intent = Intent(MyApplication.context, SelectAddressActivity::class.java)
            intent.apply {
                if (sourceType == Constants.ORDER_TYPE)
                    putExtra(Constants.SOURCE, Constants.ORDER_TYPE)
                else
                    putExtra(Constants.SOURCE, Constants.HOME_SCREEN)
                startActivity(intent)
            }
            finish()
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.check_address_box ->
                selected_id = if (check_address_box.isChecked) 1 else 0
            R.id.tv_save -> {
                if (source.isNotEmpty() && source == Constants.SOURCE_EDITADDRESS)
                    callUpdateAddress()
                else
                    addDeliveryAddress()
            }
            R.id.et_locality ->
                showLocalityDialog()
        }
    }

    private fun showLocalityDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogview = inflater.inflate(R.layout.main_locality_dialog, null)
        builder.setView(dialogview)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        myAddressListAdapter = BaseRecAdapter(this, R.layout.locality_dialog_adapter_list_item, adapterType = Constants.TYPE_MAIN_CAT_ADAPTER, adapterClickListener = this)
        if (Validation.isValidObject(addressList)) {
            myAddressListAdapter.addList(addressList)
        }
        dialogview.rv_main_loc_category.apply {
            layoutManager = linearLayoutManager
            adapter = myAddressListAdapter
        }
        dialog = builder.create()
        dialog.show()

    }

    override fun getlocality() {
        if (NetworkStatus.isOnline2(this)) {
            showViewState(MultiStateView.VIEW_STATE_LOADING)
            presenter.callgetLocality(Constants.ALL, Constants.CITY)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(MultiStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setLocalityRes(res: LocalityRes) {
        if (Validation.isValidStatus(res)) {
            addressList = res.result
            showViewState(MultiStateView.VIEW_STATE_CONTENT)
        } else
            showViewState(MultiStateView.VIEW_STATE_ERROR)
    }


    override fun showMsg(msg: String?) {
        showToastMsg(msg!!)
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

    override fun showDeliveryAddressValidateErrorMsg(msg: String) {
        when (msg) {
            "1" -> showToastMsg(getString(R.string.err_please_enter_valid_nick_name))
            "2" -> showToastMsg(getString(R.string.err_please_enter_valid_firstname))
            "3" -> showToastMsg(getString(R.string.err_please_enter_valid_lastname))
            "4" -> showToastMsg(getString(R.string.err_please_enter_valid_mobile_num))
            "5" -> showToastMsg(getString(R.string.err_please_enter_valid_locality))
            "6" -> showToastMsg(getString(R.string.err_please_enter_valid_house_number))
            "7" -> showToastMsg(getString(R.string.err_please_enter_valid_residential_complex))
            "8" -> showToastMsg(getString(R.string.err_please_enter_valid_area))
            "9" -> showToastMsg(getString(R.string.err_please_enter_valid_pin_code))
        }
    }

    override fun onclick(item: Any, pos: Int, type: String?, op: String) {
        if (item is Locality) {
            et_locality.text = item.city
            dialog.dismiss()
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