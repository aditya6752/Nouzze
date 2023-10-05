package com.screentimex.nouzze.Services

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.screentimex.nouzze.Activities.CheckOut
import com.screentimex.nouzze.Activities.ProfileActivity
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.TimeUsageData
import com.screentimex.nouzze.models.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MidNightWordManager(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val mFireStore = FirebaseFirestore.getInstance()
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val dataListJson = inputData.getString(Constants.WORK_MANAGER_INPUT_DATA)
            val gson = Gson()
            val pairType = object : TypeToken<Pair<UserDetails, TimeUsageData>>() {}.type
            val pair: Pair<UserDetails, TimeUsageData> = gson.fromJson(dataListJson, pairType)
            val mUserDetails = pair.first
            val timeUsageData = pair.second
            Log.i("MyTag", "Working")
            val updatedPoints = PointsCalculation(mUserDetails, timeUsageData).calculate()
            Log.i("MyTag", "$updatedPoints")
            val userHashMap = HashMap<String, Any>()
            userHashMap[Constants.POINTS] = updatedPoints
            updateProfileData(userHashMap)
            addUserTimeDataToFireBase(timeUsageData)
            return@withContext Result.success()
        }
    }

    private fun updateProfileData(userHashMap: HashMap<String, Any>){
        Log.i("MyTag", "Ponits")
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i("MyTag", "points Stored Successfully!!")
            }.addOnFailureListener{ e ->
                Log.e("MyTag", "Failed to Store Points")
            }
    }

    private fun addUserTimeDataToFireBase(timeUsageData: TimeUsageData) {
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        timeUsageData.time = currentTime
        mFireStore.collection(Constants.TIME)
            .document(getCurrentUUID())
            .set(timeUsageData)
            .addOnSuccessListener {
                Log.i("MyTag", "Time Data Stored Successfully!!")
            }
            .addOnFailureListener {
                Log.i("MyTag", "Failed to Store Time Data")
            }
    }

    private fun getCurrentUUID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}