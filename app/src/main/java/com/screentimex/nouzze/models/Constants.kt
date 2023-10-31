package com.screentimex.nouzze.models

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

object Constants {
    const val ID: String = "id"
    const val USERS: String = "user"
    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val PHONENUMBER: String = "phoneNumber"
    const val EMAIL: String = "email"
    const val APPICONIMAGE: String = "appIconImage"
    const val TIME: String = "appUsage-time"
    const val APPNAME: String = "appName"
    const val PRODUCT_CAT: String = "productName"
    const val ADDRESS: String = "addressDetails"
    const val PRODUCTDETAILS : String = "productDetails"
    const val INTRODETAILS: String = "introDetails"
    const val PRODUCT_SHARED_PREF: String = "mySharedPreference"
    const val ADMINPRODUCT: String = "adminProducts"
    const val POINTS: String = "points"
    const val IS_EMAIL_VERIFIED_SHARED_PREF = "isEmailVerified"

    const val USAGE_PERMISSION_SHARED_PREFS = "usageStatsPermission"
    const val MID_NIGHT_USAGE_DATA_SHARED_PREFS = "midNightSharedPrefs"
    const val MID_NIGHT_USER_DATA = "midNightUserData"

    const val WORK_MANAGER_INPUT_DATA = "workManagerInputData"
    const val NO_INTERNET_POINT_STORE = "noInternetMidNight"
    const val STORE_POINTS = "storePoints"
    const val CUSTOM_DIALOG = "dialog"
    const val FREE_POINTS = "freePoints"
    const val FREE_POINTS_BOOL = "freePointsBool"

    fun getFileExtension(activity: Activity, uri: Uri?): String?{
        return MimeTypeMap.getSingleton().
        getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}