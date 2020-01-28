package com.freshbasket.customer.screens.commonadapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var fragmentList: ArrayList<Fragment> = ArrayList()
    var titleList: ArrayList<String> = ArrayList()


    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    fun addList(fragmen: Fragment, title: String) {
        fragmentList.add(fragmen)
        titleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? = titleList[position]
}