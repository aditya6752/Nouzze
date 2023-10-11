package com.screentimex.nouzze.UsageStats

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import java.util.*
import kotlin.collections.HashMap

object Utils {
    private const val DAY_IN_MILLIS = 86400 * 1000.toLong()

    fun processTime(hour: Int, minute: Int, second: Int): Int {
        return hour * 3600 + minute * 60 + second
    }

    fun reverseProcessTime(time: Int): IntArray {
        val hourMinSec = IntArray(3)
        hourMinSec[0] = time / 3600
        val remainingTime = time % 3600
        hourMinSec[1] = remainingTime / 60
        hourMinSec[2] = remainingTime % 60
        return hourMinSec
    }

    fun getTimeSpent(context: Context, packageName: String?): HashMap<String, Int> {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val beginTime = calendar.timeInMillis
        val endTime = beginTime + DAY_IN_MILLIS
        val allEvents: MutableList<UsageEvents.Event> = ArrayList()
        val appUsageMap: HashMap<String, Int> = HashMap()

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageEvents = usageStatsManager.queryEvents(beginTime, endTime)

        while (usageEvents.hasNextEvent()) {
            val currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.packageName == packageName || packageName == null) {
                if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                    currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED
                ) {
                    allEvents.add(currentEvent)
                    val key = currentEvent.packageName
                    if (appUsageMap[key] == null)
                        appUsageMap[key] = 0
                }
            }
        }

        for (i in 0 until allEvents.size - 1) {
            val E0 = allEvents[i]
            val E1 = allEvents[i + 1]

            if (E0.eventType == UsageEvents.Event.ACTIVITY_RESUMED
                && E1.eventType == UsageEvents.Event.ACTIVITY_PAUSED
                && E0.className == E1.className
            ) {
                val diff = ((E1.timeStamp - E0.timeStamp) / 1000).toInt()
                val prev = appUsageMap[E0.packageName] ?: 0
                appUsageMap[E0.packageName] = prev + diff
            }
        }

        if (allEvents.isNotEmpty()) {
            val lastEvent = allEvents[allEvents.size - 1]
            if (lastEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                val currentRunningPackageName = lastEvent.packageName
                val diff = ((System.currentTimeMillis() - lastEvent.timeStamp) / 1000).toInt()
                val prev = appUsageMap[currentRunningPackageName] ?: 0
                appUsageMap[currentRunningPackageName] = prev + diff
                appUsageMap["current$currentRunningPackageName"] = -1
            }
        }

        return appUsageMap
    }

    fun isUsageAccessAllowed(context: Context): Boolean {
        return try {
            val packageManager: PackageManager = context.packageManager
            val applicationInfo: ApplicationInfo =
                packageManager.getApplicationInfo(context.packageName, 0)
            val appOpsManager =
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode: Int = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
