package com.freshbasket.customer.util

import android.support.v7.widget.AppCompatImageView
import com.freshbasket.customer.R
import com.freshbasket.customer.api.ApiConstants
import com.squareup.picasso.Picasso


object ImageLoader {

    fun setImage(imageView: AppCompatImageView, imageUrl: String) {
        Picasso.get()
                .load(ApiConstants.IMAGE_BASE_URL + imageUrl)
                .placeholder(R.drawable.keep_cart_logo)
                .fit()
                .error(R.drawable.keep_cart_logo)
                .into(imageView)
    }


    fun loadImagesWithoutBaseUrl(imageView: AppCompatImageView, imageUrl: String) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.keep_cart_logo)
                .fit()
                .error(R.drawable.keep_cart_logo)
                .into(imageView)
    }

}