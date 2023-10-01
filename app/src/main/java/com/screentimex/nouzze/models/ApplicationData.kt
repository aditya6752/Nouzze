package com.screentimex.nouzze.models

import android.os.Parcel
import android.os.Parcelable

data class ApplicationData(
    val appName: String = "",
    var appStartTime: Long = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appName)
        parcel.writeLong(appStartTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApplicationData> {
        override fun createFromParcel(parcel: Parcel): ApplicationData {
            return ApplicationData(parcel)
        }

        override fun newArray(size: Int): Array<ApplicationData?> {
            return arrayOfNulls(size)
        }
    }
}

