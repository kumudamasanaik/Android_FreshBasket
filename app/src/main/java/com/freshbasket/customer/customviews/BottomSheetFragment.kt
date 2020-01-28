package com.freshbasket.customer.customviews

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.ISelectedDateListener
import com.freshbasket.customer.screens.addressselection.SelectAddressActivity
import com.freshbasket.customer.util.CommonUtils
import kotlinx.android.synthetic.main.pre_order_date.*
import kotlinx.android.synthetic.main.pre_order_date.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetFragment : BottomSheetDialogFragment(),ISelectedDateListener{


    fun BottomSheetFragment() {
        // Required empty public constructor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.partial_botom_sheet, container, false)

        view.tv_pre_order_check_out.setOnClickListener {
            //startActivity(Intent(context, SelectAddressActivity::class.java))
            if (CommonUtils.isUserLogin()) {
                val intent = Intent(context, SelectAddressActivity::class.java)
                intent.apply {
                    putExtra(Constants.SOURCE, Constants.ORDER_NOW_BOTTOM_SHEET_FRAGMENT)
                }
                startActivity(intent)
            } else {
                CommonUtils.showCustomToast(this.context!!, getString(R.string.Please_login_to_the_application), 1)
            }
        }
        view.iv_calender.setOnClickListener {
             CommonUtils.showCalenderPicker(context!!, this, 0, 0)
        }

        val date = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MMM dd, yyyy")
        val dateString = sdf.format(date)
        view.tv_calender_text.text = dateString

        return view
    }

    override fun setSelectedDate(date: String) {
        tv_calender_text.text = date

    }
}