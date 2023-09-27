package com.screentimex.nouzze.models

import android.os.Parcel
import android.os.Parcelable

data class ProfileDetails(
    var id: String = "",
    var email : String = "",
    var name : String = "",
    var age: Int = 0,
    var profession: String = "",
    var image: String = "",
    var phoneNumber: Long = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeInt(age)
        parcel.writeString(profession)
        parcel.writeString(image)
        parcel.writeLong(phoneNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileDetails> {
        override fun createFromParcel(parcel: Parcel): ProfileDetails {
            return ProfileDetails(parcel)
        }

        override fun newArray(size: Int): Array<ProfileDetails?> {
            return arrayOfNulls(size)
        }
    }
}
