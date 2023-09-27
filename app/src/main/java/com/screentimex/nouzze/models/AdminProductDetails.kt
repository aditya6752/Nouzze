package com.screentimex.nouzze.models

import android.os.Parcel
import android.os.Parcelable

data class AdminProductDetails(
    var productName: String = "",
    var productPrice: String = "",
    var productImage: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productName)
        parcel.writeString(productPrice)
        parcel.writeString(productImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdminProductDetails> {
        override fun createFromParcel(parcel: Parcel): AdminProductDetails {
            return AdminProductDetails(parcel)
        }

        override fun newArray(size: Int): Array<AdminProductDetails?> {
            return arrayOfNulls(size)
        }
    }
}
