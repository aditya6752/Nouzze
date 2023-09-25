package com.screentimex.nouzze.models

import android.os.Parcel
import android.os.Parcelable
data class AddressDetails(
    var Name : String = "",
    var Mobile_Number : String = "",
    var Flat_Number : String = "",
    var Area : String = "",
    var Landmark : String = "",
    var Pincode : String = "",
    var City : String = "",
    var State : String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Name)
        parcel.writeString(Mobile_Number)
        parcel.writeString(Flat_Number)
        parcel.writeString(Area)
        parcel.writeString(Landmark)
        parcel.writeString(Pincode)
        parcel.writeString(City)
        parcel.writeString(State)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddressDetails> {
        override fun createFromParcel(parcel: Parcel): AddressDetails {
            return AddressDetails(parcel)
        }

        override fun newArray(size: Int): Array<AddressDetails?> {
            return arrayOfNulls(size)
        }
    }
}
