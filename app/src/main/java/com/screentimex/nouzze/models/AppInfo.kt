package com.screentimex.nouzze.models

import android.graphics.drawable.Drawable



data class AppInfo(
    val appName: String,
    val icon: Drawable,
    val packageName: String,
    val isTracked: Boolean,
    val isUsageExceeded: Boolean,
    var timeUseApp: Int
) : Comparable<AppInfo> {
    override fun compareTo(appInfo: AppInfo): Int {
        return appName.compareTo(appInfo.appName)
    }
}
