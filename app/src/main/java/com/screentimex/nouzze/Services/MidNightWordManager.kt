package com.screentimex.nouzze.Services

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.screentimex.nouzze.Activities.CheckOut
import com.screentimex.nouzze.Activities.ProfileActivity
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.TimeUsageData
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
            val data = gson.fromJson(dataListJson, TimeUsageData::class.java)
            val updatedPoints = PointsCalculation().calculate()
            addUserTimeDataToFireBase(data)
            return@withContext Result.success()
        }
    }

    fun updateProfileData(userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUUID())
            .update(userHashMap)
            .addOnSuccessListener {

            }.addOnFailureListener{ e ->

            }
    }

    private fun addUserTimeDataToFireBase(timeUsageData: TimeUsageData) {
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        timeUsageData.time = currentTime
        mFireStore.collection(Constants.TIME)
            .document(getCurrentUUID())
            .set(timeUsageData)
            .addOnSuccessListener {
                Log.i("MyTag", "Data Stored Successfully!!")
            }
            .addOnFailureListener {
                Log.i("MyTag", "Failed to Store Data")
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