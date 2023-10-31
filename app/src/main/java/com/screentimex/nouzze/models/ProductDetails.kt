package com.screentimex.nouzze.models

import android.os.Parcel
import android.os.Parcelable

data class ProductDetails(
    val productName: String = "",
    val productDescription: String = "",
    val productMrp: String = "",
    val productCoverImage: String = "",
    val productCategory: String = "",
    val productId: String = "",
)
