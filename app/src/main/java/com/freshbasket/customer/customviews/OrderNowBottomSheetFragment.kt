package com.freshbasket.customer.customviews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.screens.addressselection.SelectAddressActivity
import com.freshbasket.customer.util.CommonUtils
import kotlinx.android.synthetic.main.partial_order_now_bottom_sheet.*
import kotlinx.android.synthetic.main.partial_order_now_bottom_sheet.view.*

class OrderNowBottomSheetFragment : BottomSheetDialogFragment() {
    private var mContext: Context? = null
    fun BottomSheetFragment() {
        // Required empty public constructor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.partial_order_now_bottom_sheet, container, false)



        view.tv_order_check_out.setOnClickListener {
            if (CommonUtils.isUserLogin()) {
                val intent = Intent(context, SelectAddressActivity::class.java)
                intent.apply {
                    putExtra(Constants.SOURCE, Constants.ORDER_NOW_BOTTOM_SHEET_FRAGMENT)
                }
                startActivity(intent)
            } else {
                CommonUtils.showCustomToast(this.context!!, getString(R.string.Please_login_to_the_application), 1)
            }
            //startActivity(Intent(context, SelectAddressActivity::class.java))
        }


        view.standard_linear_layout.setOnClickListener {
            CommonUtils.setDeliverySelectionMethod(false)
            iv_radio_btn_on.setImageResource(R.drawable.ic_radio_button_on)
            iv_radio_btn_off.setImageResource(R.drawable.ic_radio_button_off)
        }

        view.express_linear_layout.setOnClickListener {
            CommonUtils.setDeliverySelectionMethod(true)
            iv_radio_btn_on.setImageResource(R.drawable.ic_radio_button_off)
            iv_radio_btn_off.setImageResource(R.drawable.ic_radio_button_on)
        }

        // Inflate the layout for this
        return view


    }

}