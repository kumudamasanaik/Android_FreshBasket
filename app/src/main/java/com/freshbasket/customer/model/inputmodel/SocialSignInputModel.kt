package com.freshbasket.customer.model.inputmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SocialSignInputModel(
        val first_name: String?,
        val last_name: String?,
        val email: String?,
        val mobile: String?,
        val google_id: String?,
        val facebook_id: String?,
        val pic: String?
) : Parcelable