package com.screentimex.nouzze.UsageStats

import android.app.Activity
import android.app.usage.UsageStatsManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.screentimex.nouzze.Activities.MainActivity
import com.screentimex.nouzze.Firebase.FireStoreClass
import com.screentimex.nouzze.R
import com.screentimex.nouzze.models.ApplicationData
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ScreenUsageData
import com.screentimex.nouzze.models.TimeUsageData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UsageScreenTime: AppCompatActivity(){
    private var mDefaultMapOfAllApps = Constants.MAP_PACKAGE_APP_NAME
    private var mDefaultArrayOfApps = Constants.ARRAY_LIST_ALL_APPS

    private lateinit var mMidNightUsageData: ArrayList<ApplicationData>

    data class AggregatedUsageStats(
        val packageName: String,
        var totalTimeInForeground: Long
    )
    private fun getAppUsageStats(context: Activity): Map<String, AggregatedUsageStats> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.set(2023, Calendar.OCTOBER, 1)
        val startTime = calendar.timeInMillis
        val usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY, startTime, endTime)

        // Aggregate usage stats by package name
        val aggregatedStats = mutableMapOf<String, AggregatedUsageStats>()
        for (usageStats in usageStatsList) {
            val existingStats = aggregatedStats[usageStats.packageName]
            if (existingStats == null) {
                aggregatedStats[usageStats.packageName] = AggregatedUsageStats(usageStats.packageName, usageStats.totalTimeInForeground)
            } else {
                existingStats.totalTimeInForeground += usageStats.totalTimeInForeground
            }
        }
        return aggregatedStats
    }

    fun usageDataMidNight(context: Activity): TimeUsageData {
        val usageStatsMap = getAppUsageStats(context)
        val mMidNightUsageData = Constants.ARRAY_LIST_ALL_APPS
        for ((packageName, aggregatedStats) in usageStatsMap) {
            val totalUsageTime = aggregatedStats.totalTimeInForeground / 1000 // in seconds
            if(mDefaultMapOfAllApps.containsKey(packageName)) {
                val index = mMidNightUsageData.indexOfFirst { it.appName == mDefaultMapOfAllApps[packageName] }
                mMidNightUsageData[index].appStartTime = totalUsageTime
            }
        }
        return TimeUsageData("", mMidNightUsageData)
    }

    fun updateUsageStatsOnCreate(context: Activity, myDataList: ArrayList<ApplicationData>): ArrayList<ScreenUsageData> {
        val usageStatsMap = getAppUsageStats(context)
        val res = arrayListOf<ScreenUsageData>()
        mMidNightUsageData = Constants.ARRAY_LIST_ALL_APPS
        for ((packageName, aggregatedStats) in usageStatsMap) {
            val totalUsageTime = aggregatedStats.totalTimeInForeground / 1000 // in seconds
            //if (totalUsageTime == 0L) continue
            val curHours = (totalUsageTime / 3600).toInt()
            val curMinutes = (totalUsageTime % 3600 / 60).toInt()
            val curSeconds = (totalUsageTime % 60).toInt()

            if(mDefaultMapOfAllApps.containsKey(packageName)) {
                val index = myDataList.indexOfFirst { it.appName == mDefaultMapOfAllApps[packageName] }
                mMidNightUsageData[index].appStartTime = totalUsageTime
                val prevTime = myDataList[index].appStartTime // in seconds
                if (prevTime - totalUsageTime == 0L)  continue

                val prevHours = (prevTime / 3600).toInt()
                val prevMinutes = ((prevTime % 3600) / 60).toInt()
                val prevSeconds = (prevTime % 60).toInt()

                var diffHours = curHours - prevHours
                var diffMinutes = curMinutes - prevMinutes
                var diffSeconds = curSeconds - prevSeconds

                if (diffSeconds < 0) {
                    diffSeconds += 60
                    diffMinutes -= 1
                }

                if (diffMinutes < 0) {
                    diffMinutes += 60
                    diffHours -= 1
                }
                val time = "${diffHours}h : ${diffMinutes}min : ${diffSeconds}sec"
                res.add(ScreenUsageData(R.drawable.appicon.toString(),
                    mDefaultMapOfAllApps[packageName]!!,
                    time))
            }
        }
        return res
    }
    fun usageStatsCallFireBase(context: Activity) {
        val usageStatsMap = getAppUsageStats(context)
        val timeDataList = mDefaultArrayOfApps
        for ((packageName, aggregatedStats) in usageStatsMap) {
            val totalUsageTime = aggregatedStats.totalTimeInForeground / 1000 // in seconds
            //if (totalUsageTime == 0L) continue

            if(mDefaultMapOfAllApps.containsKey(packageName)) {
                val mApplicationData = ApplicationData(mDefaultMapOfAllApps[packageName]!!, totalUsageTime)
                val index = timeDataList.indexOfFirst{it.appName == mDefaultMapOfAllApps[packageName]}
                timeDataList[index] = mApplicationData
            }
        }
        FireStoreClass().addUserTimeDataToFireBase(context, TimeUsageData("", timeDataList))
    }
}