package com.screentimex.nouzze.Services

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.TimeUsageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MidNightWordManager(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val mFireStore = FirebaseFirestore.getInstance()
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val dataListJson = inputData.getString(Constants.WORK_MANAGER_INPUT_DATA)
            val gson = Gson()
            val data = gson.fromJson(dataListJson, TimeUsageData::class.java)
            addUserTimeDataToFireBase(data)
            return@withContext Result.success()
        }
    }

    private fun addUserTimeDataToFireBase(timeUsageData: TimeUsageData) {
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