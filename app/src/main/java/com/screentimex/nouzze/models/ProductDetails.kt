package com.screentimex.nouzze.models

import android.os.Parcel
import android.os.Parcelable

data class ProductDetails(
    var productName : String,
    var productPrice : String,
    var productImg : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(productName)
        parcel.writeString(productPrice)
        parcel.writeString(productImg)
    }

    companion object CREATOR : Parcelable.Creator<ProductDetails> {
        override fun createFromParcel(parcel: Parcel): ProductDetails {
            return ProductDetails(parcel)
        }

        override fun newArray(size: Int): Array<ProductDetails?> {
            return arrayOfNulls(size)
        }
    }
}
