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
    const val TIME: String = "appUsagetime"
    const val APPNAME: String = "appName"
    const val PRODUCT_NAME: String = "productName"
    const val ADDRESS: String = "addressDetails"
    const val PRODUCTDETAILS : String = "productDetails"
    const val INTRODETAILS: String = "introDetails"
    const val SHAREDPREFERENCE: String = "mySharedPreference"
    const val ADMINPRODUCT: String = "adminProducts"
    const val POINTS: String = "points"

    fun getFileExtension(activity: Activity, uri: Uri?): String?{
        return MimeTypeMap.getSingleton().
        getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}