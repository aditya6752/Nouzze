package com.screentimex.nouzze.Services

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.TotalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MidNightWordManager(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val mFireStore = FirebaseFirestore.getInstance()
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val dataListJson = inputData.getString(Constants.WORK_MANAGER_INPUT_DATA)
            val gson = Gson()
            val totalData = gson.fromJson(dataListJson, TotalData::class.java)
            val mUserDetails = totalData.userData
            val timeUsageData = totalData.appInfoList
            //val updatedPoints = PointsCalculation(mUserDetails, timeUsageData).calculate()
            val updatedPoints = 10
            Log.i("WorkManager", "${timeUsageData.size}")
            val userHashMap = HashMap<String, Any>()
            userHashMap[Constants.POINTS] = updatedPoints
            updateProfileData(userHashMap)
            return@withContext Result.success()
        }
    }

    private fun updateProfileData(userHashMap: HashMap<String, Any>){
        Log.i("MyTag", "Points")
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i("MyTag", "points Stored Successfully!!")
            }.addOnFailureListener{ e ->
                Log.e("MyTag", "Failed to Store Points")
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