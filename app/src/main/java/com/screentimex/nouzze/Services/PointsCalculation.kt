package com.screentimex.nouzze.Services

import android.app.Activity
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.screentimex.nouzze.Activities.CheckOut
import com.screentimex.nouzze.Activities.MainActivity
import com.screentimex.nouzze.Activities.ProfileActivity
import com.screentimex.nouzze.models.ConstPoints
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.TimeUsageData
import com.screentimex.nouzze.models.UserDetails
import kotlinx.coroutines.flow.callbackFlow

class PointsCalculation(private val userDetails: UserDetails, private val timeDetails: TimeUsageData) {

    private var previousPoints = userDetails.points
    private val timeList = timeDetails.timeList
    private val profession = userDetails.profession

    fun calculate(): Long {
        val mapProfessionLimit = ConstPoints.MAP_AGE_PROFESSION_TIME
        val mapsOfPointsData = ConstPoints.MAP_EVERYTHING[profession]!!


        for ( applicationsData in timeList ){
            val applicationName = applicationsData.appName
            val applicationTime = applicationsData.appStartTime
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