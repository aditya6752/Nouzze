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
    const val PRODUCT_NAME: String = "productName"
    const val ADDRESS: String = "addressDetails"
    const val PRODUCTDETAILS : String = "productDetails"
    const val INTRODETAILS: String = "introDetails"
    const val PRODUCT_SHARED_PREF: String = "mySharedPreference"
    const val ADMINPRODUCT: String = "adminProducts"
    const val POINTS: String = "points"
    const val IS_EMAIL_VERIFIED_SHARED_PREF = "isEmailVerified"

    const val USAGE_PERMISSION_SHARED_PREFS = "usageStatsPermission"
    const val MID_NIGHT_USAGE_DATA_SHARED_PREFS = "midNightSharedPrefs"

    const val WORK_MANAGER_INPUT_DATA = "workManagerInputData"

    fun getFileExtension(activity: Activity, uri: Uri?): String?{
        return MimeTypeMap.getSingleton().
        getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    val ARRAY_LIST_ALL_APPS = arrayListOf(
        ApplicationData("Amazon", 0),
        ApplicationData("Blinkit", 0),
        ApplicationData("Facebook", 0),
        ApplicationData("Flipkart", 0),
        ApplicationData("Hotstar", 0),
        ApplicationData("Inshorts", 0),
        ApplicationData("Instagram", 0),
        ApplicationData("LinkedIn", 0),
        ApplicationData("Messenger", 0),
        ApplicationData("Myntra", 0),
        ApplicationData("Netflix", 0),
        ApplicationData("Pinterest", 0),
        ApplicationData("Prime Video", 0),
        ApplicationData("Reddit", 0),
        ApplicationData("Snapchat", 0),
        ApplicationData("Sony Liv", 0),
        ApplicationData("Spotify", 0),
        ApplicationData("Swiggy", 0),
        ApplicationData("Telegram", 0),
        ApplicationData("Threads", 0),
        ApplicationData("WhatsApp", 0),
        ApplicationData("X", 0),
        ApplicationData("YouTube", 0),
        ApplicationData("Zepto", 0),
        ApplicationData("Zomato", 0),
        ApplicationData("My App", 0)
    )

    val MAP_PACKAGE_APP_NAME = mapOf(
        "com.instagram.android" to "Instagram",
        "com.whatsapp" to "WhatsApp",
        "com.twitter.android" to "X",
        "com.facebook.katana" to "Facebook",
        "com.snapchat.android" to "Snapchat",
        "org.telegram.messenger" to "Telegram",
        "com.reddit.frontpage" to "Reddit",
        "com.linkedin.android" to "LinkedIn",
        "com.pinterest" to "Pinterest",
        "com.facebook.orca" to "Messenger",
        "com.google.android.youtube" to "YouTube",
        "com.netflix.mediaclient" to "Netflix",
        "com.amazon.avod.thirdpartyclient" to "Prime Video",
        "com.sonyliv" to "Sony Liv",
        "in.startv.hotstar" to "Hotstar",
        "com.spotify.music" to "Spotify",
        "in.amazon.mShop.android.shopping" to "Amazon",
        "com.flipkart.android" to "Flipkart",
        "com.myntra.android" to "Myntra",
        "com.nis.app" to "Inshorts",
        "com.application.zomato" to "Zomato",
        "in.swiggy.android" to "Swiggy",
        "com.blinkit.consumer" to "Blinkit",
        "com.zeptolab.ctr.ads" to "Zepto",
        "com.instagram.barcelona" to "Threads",
        "com.lke.appscreentime" to "My App"
    )


}