package com.screentimex.nouzze.models

import android.graphics.drawable.Drawable



data class AppInfo(
    val appName: String,
    val packageName: String,
    val isTracked: Boolean,
    val isUsageExceeded: Boolean,
    var timeUseApp: Long,
    var appIcon: Drawable
) : Comparable<AppInfo> {
    override fun compareTo(appInfo: AppInfo): Int {
        return appName.compareTo(appInfo.appName)
    }
}
