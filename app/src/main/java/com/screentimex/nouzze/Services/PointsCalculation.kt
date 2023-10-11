package com.screentimex.nouzze.Services

import android.util.Log
import com.screentimex.nouzze.models.AppInfo
import com.screentimex.nouzze.models.ConstPoints
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.UserDetails

class PointsCalculation(userDetails: UserDetails, timeUsageData: List<AppInfo>) {

    private var previousPoints = userDetails.points
    private val timeList = timeUsageData
    private val profession = userDetails.profession

    fun calculate(): Long {
        val mapProfessionLimit = ConstPoints.MAP_AGE_PROFESSION_TIME
        val mapsOfPointsData = ConstPoints.MAP_EVERYTHING[profession]!!

        for ( applicationsData in timeList ){
            val applicationName = applicationsData.appName
            val applicationTime = applicationsData.timeUseApp / 60
            val packageName = applicationsData.packageName


            Log.d("PointsCal", "$applicationName: $applicationTime")
            val type = ConstPoints.MAP_APP_TYPE[applicationName]
            val limit = mapProfessionLimit[type]

            if (type != null && limit != null) {
                Log.i("MyTag", "$limit $applicationName $applicationTime")
                previousPoints += if (applicationTime <= limit) {
                    mapsOfPointsData.get(type)!!.first
                } else {
                    mapsOfPointsData.get(type)!!.second
                }
            } else {
                Log.i("MyTag", "Type or limit not found for $applicationName")
            }
        }

        return previousPoints
    }

}