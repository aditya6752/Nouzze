package com.screentimex.nouzze.models

import android.os.Parcel
import android.os.Parcelable

data class TimeUsageData(
    var time: ArrayList<ApplicationData> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.createTypedArrayList(ApplicationData.CREATOR)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TimeUsageData> {
        override fun createFromParcel(parcel: Parcel): TimeUsageData {
            return TimeUsageData(parcel)
        }

        override fun newArray(size: Int): Array<TimeUsageData?> {
            return arrayOfNulls(size)
        }
    }
}
