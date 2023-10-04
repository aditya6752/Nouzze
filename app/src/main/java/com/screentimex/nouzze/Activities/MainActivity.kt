package com.screentimex.nouzze.Activities

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.screentimex.nouzze.Adapters.UsageScreenRecyclerViewAdapter
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.Firebase.SignInActivity
import com.screentimex.nouzze.R
import com.screentimex.nouzze.Services.MidNightWordManager
import com.screentimex.nouzze.UsageStats.UsageScreenTime
import com.screentimex.nouzze.databinding.ActivityDrawerBinding
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.UserDetails
import com.screentimex.nouzze.models.ScreenUsageData
import com.screentimex.nouzze.models.TimeUsageData
import com.screentimex.nouzze.models.TotalData
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawerBinding
    private lateinit var mUser: FirebaseUser
    private lateinit var mUserDetails: UserDetails
    private lateinit var mSharedPreferences: SharedPreferences

    private lateinit var mTimeUsageList: ArrayList<ScreenUsageData>

    companion object {
        const val MY_PROFILE_REQ_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        // email verification
        mUser = FirebaseAuth.getInstance().currentUser!!
        mSharedPreferences = getSharedPreferences(Constants.USAGE_PERMISSION_SHARED_PREFS, Context.MODE_PRIVATE)
        FireStoreClass().loadUserData(this@MainActivity)

        binding.apply {
            shareButton.setOnClickListener {
                showToast("Share Button Working")
            }
            settingsButton.setOnClickListener {
                showToast("Settings Button Working")
            }
            helpButton.setOnClickListener {
                showToast("Help Button Working")
            }
            navDraawerHeaderInclude.logOutButton.setOnClickListener {
                signOut()
            }
            navDraawerHeaderInclude.userImageNavHeader.setOnClickListener {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivityForResult(intent, MY_PROFILE_REQ_CODE)
            }
            includeAppBarLayout.MainScreenUsageActivity.marketPlaceButton.setOnClickListener {
                val intent = Intent(this@MainActivity, StoreActivity::class.java)
                startActivity(intent)
            }
            includeAppBarLayout.MainScreenUsageActivity.verifyEmailButton.setOnClickListener {
                FireStoreClass().sendEmailVerificationLink(this@MainActivity, mUser)
            }
            includeAppBarLayout.MainScreenUsageActivity.givePermissionButton.setOnClickListener {
                askForUsageAccessPermission()
            }
        }


    }


    private fun midNightWorkScheduler(timeUsageData: TimeUsageData) {
        val currentTime = Calendar.getInstance()
        val midnight = Calendar.getInstance()
        midnight.add(Calendar.DAY_OF_YEAR, 1)
        midnight.set(Calendar.HOUR_OF_DAY, 0)
        midnight.set(Calendar.MINUTE, 0)
        midnight.set(Calendar.SECOND, 0)
        val timeDifferenceMillis = midnight.timeInMillis - currentTime.timeInMillis

        val gson = Gson()
        val userDataJson = gson.toJson(TotalData(mUserDetails, timeUsageData))

        val workRequest = OneTimeWorkRequest.Builder(MidNightWordManager::class.java)
            .setInputData(Data.Builder().putString(Constants.WORK_MANAGER_INPUT_DATA, userDataJson).build())
            .setInitialDelay(timeDifferenceMillis, TimeUnit.MILLISECONDS) // Delay until midnight
            .build()

        // Schedule the task
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun permissionGranted() {
         if(isUsageStatsPermissionGranted()) {
            binding.apply {
                includeAppBarLayout.MainScreenUsageActivity.permissionTextView.visibility = View.INVISIBLE
                includeAppBarLayout.MainScreenUsageActivity.givePermissionButton.visibility = View.INVISIBLE
                includeAppBarLayout.MainScreenUsageActivity.marketPlaceButton.isEnabled = true

                val mPermissionGrantedFirstTime = mSharedPreferences.getBoolean("FireBase", false)
                if(!mPermissionGrantedFirstTime) {
                    val editor = mSharedPreferences.edit()
                    editor.putBoolean("FireBase", true)
                    editor.apply()
                    // Call firebaseFunction() if it hasn't been called yet
                    binding.includeAppBarLayout.MainScreenUsageActivity.permissionTextView.visibility = View.VISIBLE
                    binding.includeAppBarLayout.MainScreenUsageActivity.permissionTextView.text = "Restart Your App To Get Time Usage Data"
                    UsageScreenTime().usageStatsCallFireBase(this@MainActivity)
                }
                else {
                    binding.includeAppBarLayout.MainScreenUsageActivity.permissionTextView.visibility = View.INVISIBLE
                    FireStoreClass().getUserTimeDataFromFireBase(this@MainActivity)
                }
            }
         } else {
             binding.apply {
                 includeAppBarLayout.MainScreenUsageActivity.permissionTextView.visibility = View.VISIBLE
                 includeAppBarLayout.MainScreenUsageActivity.givePermissionButton.visibility = View.VISIBLE
             }
         }
    }

    private fun isUsageStatsPermissionGranted(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
    private fun askForUsageAccessPermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    }
    private fun setUpRecyclerView(list: ArrayList<ScreenUsageData>) {
        binding.includeAppBarLayout.MainScreenUsageActivity.mainScreenRecyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity)
        val mAdapter = UsageScreenRecyclerViewAdapter(this, list)
        binding.includeAppBarLayout.MainScreenUsageActivity.mainScreenRecyclerView.adapter = mAdapter
    }

    private fun setUpActionBar(){
        setSupportActionBar(binding.includeAppBarLayout.toolbar)
        binding.includeAppBarLayout.toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        binding.includeAppBarLayout.toolbar.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer(){
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode== MY_PROFILE_REQ_CODE){
            FireStoreClass().loadUserData(this)
            toggleDrawer()
        }
        else{
            Log.e("Cancelled","Cancelled")
        }
    }

    fun updateNavigationUserDetails(user: UserDetails){
        mUserDetails = user
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_holder)
            .into(binding.navDraawerHeaderInclude.userImageNavHeader)
        binding.navDraawerHeaderInclude.userEmailNavHeader.text = user.email
        binding.navDraawerHeaderInclude.userNameNavHeader.text = user.name
        binding.includeAppBarLayout.MainScreenUsageActivity.userPoints.text = "Points: " + user.points.toString()
    }


    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun checkEmailVerifiedOrNot() {
        mUser.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mUser = FirebaseAuth.getInstance().currentUser!! // Refresh the user object

                if (mUser.isEmailVerified) {
                    // User's email is verified
                    binding.apply {
                        includeAppBarLayout.MainScreenUsageActivity.verifyEmailButton.visibility = View.GONE
                        includeAppBarLayout.MainScreenUsageActivity.permissionTextView.visibility = View.VISIBLE
                        includeAppBarLayout.MainScreenUsageActivity.givePermissionButton.visibility = View.VISIBLE
                    }
                } else {
                    // User's email is not verified
                    binding.apply {
                        includeAppBarLayout.MainScreenUsageActivity.mainScreenRecyclerView.visibility = View.INVISIBLE
                        includeAppBarLayout.MainScreenUsageActivity.verifyEmailButton.visibility = View.VISIBLE
                        includeAppBarLayout.MainScreenUsageActivity.marketPlaceButton.isEnabled = false
                    }
                }
            } else {
                // Error occurred while reloading user data
                // Handle the error, check task.exception for details
                val errorMessage = task.exception?.message ?: "Unknown error"
                Log.e("EmailVerification", "Error: $errorMessage")
            }
        }
    }


    fun emailVerificationLinkSendSuccessfully() {
        Toast.makeText(this@MainActivity, "Verification link sent. Please check your email", Toast.LENGTH_LONG).show()
        checkEmailVerifiedOrNot()
    }

    fun emailVerificationLinkSendFailed() {
        Toast.makeText(this@MainActivity, "Network Issue!!", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        mUser.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mUser = FirebaseAuth.getInstance().currentUser!!
                if (!mUser.isEmailVerified) {
                    binding.apply {
                        includeAppBarLayout.MainScreenUsageActivity.mainScreenRecyclerView.visibility = View.INVISIBLE
                        includeAppBarLayout.MainScreenUsageActivity.verifyEmailButton.visibility = View.VISIBLE
                        includeAppBarLayout.MainScreenUsageActivity.marketPlaceButton.isEnabled = false
                    }
                } else {
                    binding.includeAppBarLayout.MainScreenUsageActivity.verifyEmailButton.visibility = View.INVISIBLE
                    permissionGranted()
                }
            }
        }
    }
    fun getUserAppData(timeData: TimeUsageData) {
        mTimeUsageList = UsageScreenTime().updateUsageStatsOnCreate(this@MainActivity, timeData.timeList)
        binding.includeAppBarLayout.MainScreenUsageActivity.mainScreenRecyclerView.visibility = View.VISIBLE
        setUpRecyclerView(mTimeUsageList)
        midNightWorkScheduler(UsageScreenTime().usageDataMidNight(this@MainActivity))
    }
    fun failedToGetPrevData(error: String) {
        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
    }
}