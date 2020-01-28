package com.freshbasket.customer.model

import android.os.Parcel
import android.os.Parcelable

data class SubCategory(
        val _id: String?,
        val description: String?,
        val name: String?,
        val pic: String?,
        val subcategory: ArrayList<SubCategory>? = null

) : Parcelable {
    fun isHavingSubList(): Boolean {
        subcategory?.apply {
            return subcategory.isNotEmpty()
        }
        return false

    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            ArrayList<SubCategory>().apply { source.readList(this, SubCategory::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(_id)
        writeString(description)
        writeString(name)
        writeString(pic)
        writeList(subcategory)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SubCategory> = object : Parcelable.Creator<SubCategory> {
            override fun createFromParcel(source: Parcel): SubCategory = SubCategory(source)
            override fun newArray(size: Int): Array<SubCategory?> = arrayOfNulls(size)
        }
    }
}