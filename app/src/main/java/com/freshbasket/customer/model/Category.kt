package com.freshbasket.customer.model

import android.os.Parcel
import android.os.Parcelable

data class Category(
        val _id: String?,
        val description: String?,
        val name: String?,
        val pic: String?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(_id)
        writeString(description)
        writeString(name)
        writeString(pic)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(source: Parcel): Category = Category(source)
            override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)
        }
    }
}