package com.screentimex.nouzze.Activities

import android.app.Activity
import android.app.AppOpsManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.screentimex.nouzze.Adapters.AppInfoListAdapter
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.Firebase.SignInActivity
import com.screentimex.nouzze.R
import com.screentimex.nouzze.Services.MidNightUsageStateSharedPref
import com.screentimex.nouzze.Services.MidNightWordManager
import com.screentimex.nouzze.Services.PointsCalculation
import com.screentimex.nouzze.UsageStats.Utils
import com.screentimex.nouzze.databinding.ActivityDrawerBinding
import com.screentimex.nouzze.models.AppInfo
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.UserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawerBinding
    private lateinit var mUser: FirebaseUser
    private lateinit var mUserDetails: UserDetails
    private lateinit var mSharedPreferences: SharedPreferences

    private lateinit var tapTargetSequence: TapTargetSequence
    private lateinit var mSharedPrefMidNightUserDetails: MidNightUsageStateSharedPref
    private lateinit var mSharedPrefPointsStoreMidNight: SharedPreferences
    private lateinit var mSharedPrefAboutAppDialog: SharedPreferences
    private lateinit var mSharedPrefFreePoints: SharedPreferences
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
        mSharedPrefMidNightUserDetails = MidNightUsageStateSharedPref(this@MainActivity)

        mSharedPrefPointsStoreMidNight = getSharedPreferences(Constants.STORE_POINTS, Context.MODE_PRIVATE)
        mSharedPrefAboutAppDialog = getSharedPreferences(Constants.CUSTOM_DIALOG, Context.MODE_PRIVATE)
        mSharedPrefFreePoints = getSharedPreferences(Constants.FREE_POINTS, Context.MODE_PRIVATE)

        if(!mSharedPrefAboutAppDialog.getBoolean(Constants.CUSTOM_DIALOG, false)) {
            showCustomDialog()
        }

        if(!mSharedPrefFreePoints.getBoolean(Constants.FREE_POINTS_BOOL, false)) {
            val editorFreePoints = mSharedPrefFreePoints.edit()
            editorFreePoints.putBoolean(Constants.FREE_POINTS_BOOL, true)
            editorFreePoints.putInt(Constants.FREE_POINTS, 50)
            editorFreePoints.apply()
            editorFreePoints.commit()
        }

        if(mSharedPrefPointsStoreMidNight.contains(Constants.NO_INTERNET_POINT_STORE)
            && isInternetConnected(this@MainActivity)) {
            Log.e("Share", "Working")
            val updatedPoints = mSharedPrefPointsStoreMidNight.getLong(Constants.NO_INTERNET_POINT_STORE, 0L)
            val userHashMap = HashMap<String, Any>()
            userHashMap[Constants.POINTS] = updatedPoints
            val editor = mSharedPrefPointsStoreMidNight.edit()
            editor.remove(Constants.NO_INTERNET_POINT_STORE)
            editor.clear()
            editor.apply()
            editor.commit()
            FireStoreClass().updateProfileData(this, userHashMap)
        }

        if( !isInternetConnected(this) ){
            binding.includeAppBarLayout.MainScreenUsageActivity.progressBarButton.visibility = View.GONE
            showSnackBar("No Internet !!")
        }

        binding.apply {
            shareButton.setOnClickListener {
                shareAppLinkRecommendFriend()
            }
            settingsButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
            helpButton.setOnClickListener {
                openGmailHelpButton()
            }
            tutorialButton.setOnClickListener {
                showTutorial()
            }
            freePointsButton.setOnClickListener {
                getFreePoints()
            }
            aboutAppButton.setOnClickListener {
                showCustomDialog()
            }
            leaderboard.setOnClickListener {
                startActivity(Intent(this@MainActivity, LeaderBoard::class.java))
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

    private fun showTutorial() {
        val targetList = listOf(
            getTapTarget(
                binding.navDraawerHeaderInclude.userImageNavHeader,
                "Go To Your Profile"),
            getTapTarget(
                binding.includeAppBarLayout.MainScreenUsageActivity.marketPlaceButton,
                "Go To Market Place"),
            getTapTarget(
                binding.includeAppBarLayout.MainScreenUsageActivity.userPoints,
                "Your Points")
        )
        tapTargetSequence = createTapTargetSequence(targetList)
        tapTargetSequence.start()
    }
    private fun getTapTarget(view: View, title: String): TapTarget {
        return TapTarget.forView(view, title)
            .transparentTarget(true)
            .cancelable(false)
            .targetRadius(80)
            .outerCircleColor(R.color.actionBarColor)
    }
    private fun createTapTargetSequence(targets: List<TapTarget>): TapTargetSequence {
        return TapTargetSequence(this)
            .targets(targets)
            .listener(object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                    showToast("Tutorial Finished")
                }

                override fun onSequenceStep(target: TapTarget, targetClicked: Boolean) {
                    Log.e("TargetSequence", "Finished")
                    if(targetClicked) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }

                override fun onSequenceCanceled(lastTarget: TapTarget?) {
                    Log.e("TargetSequence", "Cancelled")
                }
            })
    }
    private fun midNightWorkScheduler(timeUsageData: List<AppInfo>) {
        val currentTime = Calendar.getInstance()
        val midnight = Calendar.getInstance()
        midnight.add(Calendar.DAY_OF_YEAR, 1)
        midnight.set(Calendar.HOUR_OF_DAY, 0)
        midnight.set(Calendar.MINUTE, 0)
        midnight.set(Calendar.SECOND, 0)
        val timeDifferenceMillis = midnight.timeInMillis - currentTime.timeInMillis

        val userDetails = mSharedPrefMidNightUserDetails.getDataObject(Constants.MID_NIGHT_USER_DATA)
        var updatedPoints = PointsCalculation(userDetails, timeUsageData).calculate()
        if(updatedPoints <= 0) {
            updatedPoints = 53
        }
        Log.i("WorkManager", "Main")
        val workRequest = OneTimeWorkRequest.Builder(MidNightWordManager::class.java)
            .setInputData(Data.Builder().putLong(Constants.WORK_MANAGER_INPUT_DATA, updatedPoints).build())
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
                setUpRecyclerView()
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
    private fun setUpRecyclerView() {
        if(binding.includeAppBarLayout.MainScreenUsageActivity.mainScreenRecyclerView.adapter == null) {
            binding.includeAppBarLayout.MainScreenUsageActivity.progressBarButton.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Default).launch {
                val appInfoList: List<AppInfo> = getAppInfoList()
                withContext(Dispatchers.Main) {
                    midNightWorkScheduler(appInfoList)
                    val mAdapter1 = AppInfoListAdapter(this@MainActivity, appInfoList)
                    binding.includeAppBarLayout.MainScreenUsageActivity.mainScreenRecyclerView.adapter = mAdapter1
                    binding.includeAppBarLayout.MainScreenUsageActivity.progressBarButton.visibility = View.GONE
                }
            }
        }
    }
    private fun setUpActionBar(){
        setSupportActionBar(binding.includeAppBarLayout.toolbar)
        supportActionBar?.title = "Nouzze"
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
        mSharedPrefMidNightUserDetails.saveDataObject(Constants.MID_NIGHT_USER_DATA, mUserDetails)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_holder)
            .into(binding.navDraawerHeaderInclude.userImageNavHeader)
        binding.navDraawerHeaderInclude.userEmailNavHeader.text = user.email
        binding.navDraawerHeaderInclude.userNameNavHeader.text = user.name
        binding.includeAppBarLayout.MainScreenUsageActivity.userPoints.text = "Points: " + user.points.toString()
        binding.includeAppBarLayout.MainScreenUsageActivity.userIntroText.text = "Hi ${user.name}, your screentime activity is"
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

    private fun shareAppLinkRecommendFriend() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain" // Set the type to plain text for sharing a link
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.screentimex.nouzze") // Replace with the URL you want to share
        startActivity(Intent.createChooser(shareIntent, "Share Link"))
    }
    private fun openGmailHelpButton() {
        val email = "nouzzehelp@gmail.com"
        val intent = Intent(Intent.ACTION_VIEW,Uri.parse("mailto:$email"))
        startActivity(intent)
    }
    private fun getFreePoints() {
        if(mSharedPrefFreePoints.contains(Constants.FREE_POINTS)) {
            val pointsHashMap = HashMap<String, Any>()
            pointsHashMap[Constants.POINTS] = mSharedPrefFreePoints.getInt(Constants.FREE_POINTS, 0) + mUserDetails.points
            val editorFreePoints = mSharedPrefFreePoints.edit()
            editorFreePoints.remove(Constants.FREE_POINTS)
            editorFreePoints.apply()
            editorFreePoints.commit()
            showToast("Claimed Successfully")
            FireStoreClass().updateProfileData(this@MainActivity, pointsHashMap)
        } else {
            showToast("Already claimed, come back tomorrow")
        }
    }
    fun showToast(error: String) {
        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
    }
    fun showSnackBar(message: String){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.snackBarColor
            )
        )
        snackBar.show()
    }
    private fun getAppInfoList(): List<AppInfo> {
        val packageManager: PackageManager = packageManager
        val packageInfoList: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        val appInfoList: MutableList<AppInfo> = ArrayList()

        for (i in packageInfoList.indices) {
            val packageInfo: PackageInfo = packageInfoList[i]
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                val appName: String = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                val appIcon: Drawable = packageInfo.applicationInfo.loadIcon(packageManager)
                val packageName: String = packageInfo.packageName

                val useTime = getTimeUsage(packageName).toLong()
                appInfoList.add(AppInfo(appName, packageName, false, false, useTime, appIcon))
            }
        }
        appInfoList.sort()
        return appInfoList
    }
    private fun getTimeUsage(packageName: String): Int {
        val appUsageMap: HashMap<String, Int> = Utils.getTimeSpent(this@MainActivity, packageName)
        var usageTime: Int? = appUsageMap[packageName]
        if (usageTime == null) usageTime = 0
        return usageTime
    }

    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_main_screen,null )
        val dialogText = dialogView.findViewById<TextView>(R.id.messageTextView)
        val text = "Welcome to Nouzze. \uD83D\uDC80 \nWe're excited to have you on board. With Nouzze, you can earn coins by using other apps. Here's a quick guide to get you started:\n" +
                "\n" +
                "Use other apps like Snapchat, Instagram, zomato and many more.\n" +
                "The more time you spend on those apps, the more coins you'll earn.\n" +
                "Collect coins and redeem rewards in our store.\n" +
                "If you have any questions or need assistance, don't hesitate to contact our support team at (nouzzehelp@gmail.com). Happy earning!" +
                "\n" +  "\n" + "An app in which you can earn, without using the app itself."

        val spannableText = SpannableStringBuilder(text)

        val startIndex = text.indexOf("An app in which you can earn, without using the app itself.")
        val endIndex = startIndex + "An app in which you can earn, without using the app itself.".length

        spannableText.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        dialogText.text = spannableText

        builder.setView(dialogView)
            .setCancelable(false)
            .setPositiveButton("Ok Got It !!"){ _, _ ->
                val editor = mSharedPrefAboutAppDialog.edit()
                editor.putBoolean(Constants.CUSTOM_DIALOG, true)
                editor.apply()
            }.show()
    }
}