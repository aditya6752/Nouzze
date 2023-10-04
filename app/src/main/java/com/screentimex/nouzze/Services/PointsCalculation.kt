package com.screentimex.nouzze.Services

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.screentimex.nouzze.Activities.CheckOut
import com.screentimex.nouzze.Activities.MainActivity
import com.screentimex.nouzze.Activities.ProfileActivity
import com.screentimex.nouzze.models.ConstPoints
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.TimeUsageData
import com.screentimex.nouzze.models.UserDetails

class PointsCalculation(private val timeDetails: TimeUsageData) {
    private val userDetails = getUserDetails()
    private var previousPoints = userDetails.points
    private val timeList = timeDetails.timeList
    private val age = userDetails.age
    private val profession = userDetails.profession
    private val applicationTypes = ConstPoints.MAP_APP_TYPE

    private fun ageBracket(): String {
        if(age in 10..22) return "10-22"
        return "23-60"
    }

    fun calculate(): Long {
        val specificAgeBracket = ageBracket()
        val mapProfessionLimit = ConstPoints.MAP_AGE_PROFESSION_TIME[specificAgeBracket]
        val mapsOfPointsData = ConstPoints.MAP_EVERYTHING[specificAgeBracket]?.get(userDetails.profession)

        for ( applicationsData in timeList ){
            val applicationName = applicationsData.appName
            val applicationTime = applicationsData.appStartTime
            val type = ConstPoints.MAP_APP_TYPE[applicationName]
            previousPoints += if(applicationTime <= mapProfessionLimit?.get(type)!!) {
                mapsOfPointsData?.get(type)?.first!!
            } else {
                mapsOfPointsData?.get(type)?.second!!
            }
        }
        return previousPoints
    }

    fun loadUserData(){
        FirebaseFirestore.getInstance().collection(Constants.USERS)
            .document(getCurrentUUID())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(UserDetails::class.java)

            }.addOnFailureListener {
                    _ ->
                Log.e("FireStoreClassSignUp","Error")
            }
    }
    fun getCurrentUUID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}