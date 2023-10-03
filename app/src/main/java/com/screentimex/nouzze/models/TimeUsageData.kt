package com.screentimex.nouzze.models

import android.os.Parcel
import android.os.Parcelable

data class TimeUsageData(
    var time: String = "",
    var timeList: ArrayList<ApplicationData> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readString()!!,
        parcel.createTypedArrayList(ApplicationData.CREATOR)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(time)
        parcel.writeTypedList(timeList)
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
