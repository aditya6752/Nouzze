package com.screentimex.nouzze.Fragments

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.firebase.auth.FirebaseUser
import com.screentimex.nouzze.Adapters.AppInfoListAdapter
import com.screentimex.nouzze.R
import com.screentimex.nouzze.Services.MidNightUsageStateSharedPref
import com.screentimex.nouzze.Services.MidNightWordManager
import com.screentimex.nouzze.Services.PointsCalculation
import com.screentimex.nouzze.UsageStats.Utils
import com.screentimex.nouzze.databinding.FragmentScreenTimeBinding
import com.screentimex.nouzze.models.AppInfo
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.UserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ScreenTimeFragment : Fragment() {

    private lateinit var binding : FragmentScreenTimeBinding
    private lateinit var mUser: FirebaseUser
    private lateinit var mUserDetails: UserDetails
    private lateinit var mSharedPreferences: SharedPreferences

    private lateinit var tapTargetSequence: TapTargetSequence
    private lateinit var mSharedPrefMidNightUserDetails: MidNightUsageStateSharedPref
    private lateinit var mSharedPrefPointsStoreMidNight: SharedPreferences
    private lateinit var mSharedPrefAboutAppDialog: SharedPreferences
    private lateinit var mSharedPrefFreePoints: SharedPreferences

    private lateinit var mContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScreenTimeBinding.inflate(inflater,container,false)

        binding.mainScreenRecyclerView.visibility = View.VISIBLE


        return binding.root
    }

    fun setHostContext(context: Context){
        mContext = context
        setUpRecyclerView()
    }

    private fun askForUsageAccessPermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    }
    private fun permissionGranted() {
        if(isUsageStatsPermissionGranted()) {
            binding.apply {
                permissionTextView.visibility = View.INVISIBLE
                givePermissionButton.visibility = View.INVISIBLE
                marketPlaceButton.isEnabled = true
                mainScreenRecyclerView.visibility = View.VISIBLE
                setUpRecyclerView()
            }
        } else {
            binding.apply {
                permissionTextView.visibility = View.VISIBLE
                givePermissionButton.visibility = View.VISIBLE
            }
        }
    }
    private fun isUsageStatsPermissionGranted(): Boolean {
        val appOps = mContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            mContext.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
    fun setUpRecyclerView() {

        Log.i("Ritik","Hello , outside coroutine")

            binding.progressBarButton.visibility = View.VISIBLE

                val appInfoList: List<AppInfo> = getAppInfoList()
                Log.i("Ritik","Hello , coroutine")

                    midNightWorkScheduler(appInfoList)
                    val mAdapter1 = AppInfoListAdapter(mContext, appInfoList)
                    binding.mainScreenRecyclerView.adapter = mAdapter1
                    binding.progressBarButton.visibility = View.GONE


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
        WorkManager.getInstance(mContext).enqueue(workRequest)
    }
    private fun getAppInfoList(): List<AppInfo> {
        val packageManager: PackageManager = mContext.packageManager
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
        val appUsageMap: HashMap<String, Int> = Utils.getTimeSpent(mContext, packageName)
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

}