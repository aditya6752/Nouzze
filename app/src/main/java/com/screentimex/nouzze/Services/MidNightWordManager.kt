package com.screentimex.nouzze.Services

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.screentimex.nouzze.models.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MidNightWordManager(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val mFireStore = FirebaseFirestore.getInstance()
    private val mSharedPrefMidNightPointsUpdate = MidNightUsageStateSharedPref(context)
    private val mSharedPrefPointsStoreMidNight = context.getSharedPreferences("STORE_POINTS", Context.MODE_PRIVATE)
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val updatedPoints = inputData.getLong(Constants.WORK_MANAGER_INPUT_DATA, 0L)
            val userDataFromSharedPref = mSharedPrefMidNightPointsUpdate.getDataObject(Constants.MID_NIGHT_USER_DATA)
            userDataFromSharedPref.points = updatedPoints
            mSharedPrefMidNightPointsUpdate.saveDataObject(Constants.MID_NIGHT_USER_DATA, userDataFromSharedPref)

            Log.i("lke", "1")
            if(isInternetConnected(applicationContext)) {
                val userHashMap = HashMap<String, Any>()
                userHashMap[Constants.POINTS] = updatedPoints
                updateProfileData(userHashMap)
            } else {
                val editor = mSharedPrefPointsStoreMidNight.edit()
                editor.putLong(Constants.NO_INTERNET_POINT_STORE, updatedPoints)
                editor.apply()
            }
            return@withContext Result.success()
        }
    }

    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
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