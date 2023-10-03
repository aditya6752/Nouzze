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

    val MAP_APP_TYPE = mapOf(
        "Instagram" to "Influencers",
        "X" to "Influencers",
        "Threads" to "Influencers",
        "WhatsApp" to "Messaging",
        "Snapchat" to "Messaging",
        "Telegram" to "Messaging",
        "Messenger" to "Messaging",
        "Call" to "Messaging",
        "LinkedIn" to "Productivity social media app",
        "Reddit" to "Productivity social media app",
        "YouTube" to "Free Entertainment",
        "Hotstar" to "Free Entertainment",
        "Spotify" to "Free Entertainment",
        "Netflix" to "Paid Entertainment",
        "Prime Video" to "Paid Entertainment",
        "Sony Liv" to "Paid Entertainment",
        "Amazon" to "Shopping",
        "Flipkart" to "Shopping",
        "Myntra" to "Shopping",
        "Zomato" to "Grocery and Food",
        "Swiggy" to "Grocery and Food",
        "Zepto" to "Grocery and Food",
        "Blinkit" to "Grocery and Food",
        "Ola" to "Travel",
        "Uber" to "Travel",
        "Rapido" to "Travel"
    )

    val MAP_AGE_PROFESSION_TYPE = mapOf<String, Map<String, Int>>(
        "10-14" to mapOf("Influencers" to 35,
            "Messaging" to 40, "Productivity social media app" to 64,
            "Free Entertainment" to 45, "Paid Entertainment" to 33,
            "Shopping" to 45, "Grocery and Food" to 60, "Travel" to 57),
        "15-22" to mapOf("Influencers" to 65,
            "Messaging" to 55, "Productivity social media app" to 123,
            "Free Entertainment" to 120, "Paid Entertainment" to 28,
            "Shopping" to 63, "Grocery and Food" to 66, "Travel" to 121),
        "23-35" to mapOf("Influencers" to 45,
            "Messaging" to 127, "Productivity social media app" to 125,
            "Free Entertainment" to 94, "Paid Entertainment" to 64,
            "Shopping" to 120, "Grocery and Food" to 97, "Travel" to 189),
        "36-60" to mapOf("Influencers" to 48,
            "Messaging" to 86, "Productivity social media app" to 55,
            "Free Entertainment" to 99, "Paid Entertainment" to 65,
            "Shopping" to 115, "Grocery and Food" to 126, "Travel" to 111),
        "60+" to mapOf("Influencers" to 34,
            "Messaging" to 37, "Productivity social media app" to 33,
            "Free Entertainment" to 69, "Paid Entertainment" to 52,
            "Shopping" to 61, "Grocery and Food" to 37, "Travel" to 58)
    )

    val MAP_EVERYTHING = mapOf<String, Map<String, Map<String, Pair<Int, Int>>>>(
        "10-14" to mapOf("Engineering" to mapOf("Influencers" to Pair(80, -30),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(70, -20), "Paid Entertainment" to Pair(60, -30),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(70, 0)),
            "Banking" to mapOf("Influencers" to Pair(80, -30),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(70, -20), "Paid Entertainment" to Pair(60, -30),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(70, 0)),
            "Finance" to mapOf("Influencers" to Pair(80, -30),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(70, -20), "Paid Entertainment" to Pair(60, -30),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(70, 0)),
            "Doctors" to mapOf("Influencers" to Pair(80, -30),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(70, -20), "Paid Entertainment" to Pair(60, -30),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(70, 0)),

            "Teaching and Education" to mapOf("Influencers" to Pair(80, -30),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(70, -20), "Paid Entertainment" to Pair(60, -30),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(70, 0)),

            "Business" to mapOf("Influencers" to Pair(90, -20),
                "Messaging" to Pair(90, -10), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(80, -10),
                "Shopping" to Pair(100, 0), "Grocery and Food" to Pair(90, -10), "Travel" to Pair(100, 0)),

            "Media and Entertainment" to mapOf("Influencers" to Pair(80, 0),
                "Messaging" to Pair(80, 0), "Productivity social media app" to Pair(90, 0),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(80, -20),
                "Shopping" to Pair(90, -10), "Grocery and Food" to Pair(90, -20), "Travel" to Pair(90, 10)),

            "Sales and Marketing" to mapOf("Influencers" to Pair(100, -10),
                "Messaging" to Pair(100, 0), "Productivity social media app" to Pair(90, -10),
                "Free Entertainment" to Pair(70, -10), "Paid Entertainment" to Pair(70, -20),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(90, -20), "Travel" to Pair(90, -10)),

            "Social Work" to mapOf("Influencers" to Pair(100, 0),
                "Messaging" to Pair(90, -10), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(80, -20), "Paid Entertainment" to Pair(80, -30),
                "Shopping" to Pair(80, -20), "Grocery and Food" to Pair(80, -30), "Travel" to Pair(70, -10)),

            "Sports and Athletics" to mapOf("Influencers" to Pair(70, -30),
                "Messaging" to Pair(80, -30), "Productivity social media app" to Pair(90, -10),
                "Free Entertainment" to Pair(70, -20), "Paid Entertainment" to Pair(70, -30),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -20), "Travel" to Pair(80, 0)),

            "Driver" to mapOf("Influencers" to Pair(80, -30),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(70, -20), "Paid Entertainment" to Pair(60, -30),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(70, 0)),

            "Student" to mapOf("Influencers" to Pair(80, -30),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(70, -20), "Paid Entertainment" to Pair(60, -30),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(70, 0)),

            "House Maker" to mapOf("Influencers" to Pair(90, -10),
                "Messaging" to Pair(100, -10), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(90, -10), "Paid Entertainment" to Pair(90, -20),
                "Shopping" to Pair(90, -10), "Grocery and Food" to Pair(90, -10), "Travel" to Pair(80, -20)),

            "Others" to mapOf("Influencers" to Pair(80, -20),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, 10),
                "Free Entertainment" to Pair(100, -20), "Paid Entertainment" to Pair(90, -10),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(80, 10))
            )
        ,
        "15-22" to mapOf("Engineering" to mapOf("Influencers" to Pair(60, -30),
                "Messaging" to Pair(70, -30), "Productivity social media app" to Pair(80, 0),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(80, -20),
                "Shopping" to Pair(70, -20), "Grocery and Food" to Pair(80, -20), "Travel" to Pair(90, -10)),
            "Banking" to mapOf("Influencers" to Pair(60, -30),
                "Messaging" to Pair(70, -30), "Productivity social media app" to Pair(80, 0),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(80, -20),
                "Shopping" to Pair(70, -20), "Grocery and Food" to Pair(80, -20), "Travel" to Pair(90, -10)),
            "Finance" to mapOf("Influencers" to Pair(60, -30),
                "Messaging" to Pair(70, -30), "Productivity social media app" to Pair(80, 0),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(80, -20),
                "Shopping" to Pair(70, -20), "Grocery and Food" to Pair(80, -20), "Travel" to Pair(90, -10)),
            "Doctors" to mapOf("Influencers" to Pair(60, -30),
                "Messaging" to Pair(70, -30), "Productivity social media app" to Pair(80, 0),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(80, -20),
                "Shopping" to Pair(70, -20), "Grocery and Food" to Pair(80, -20), "Travel" to Pair(90, -10)),

            "Teaching and Education" to mapOf("Influencers" to Pair(60, -30),
                "Messaging" to Pair(70, -30), "Productivity social media app" to Pair(90, -10),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(80, -20),
                "Shopping" to Pair(70, -20), "Grocery and Food" to Pair(80, -20), "Travel" to Pair(90, -20)),

            "Business" to mapOf("Influencers" to Pair(70, 10),
                "Messaging" to Pair(80, 10), "Productivity social media app" to Pair(100, 10),
                "Free Entertainment" to Pair(90, 10), "Paid Entertainment" to Pair(80, 10),
                "Shopping" to Pair(80, 10), "Grocery and Food" to Pair(90, 10), "Travel" to Pair(90, 10)),

            "Media and Entertainment" to mapOf("Influencers" to Pair(90, 20),
                "Messaging" to Pair(100, 30), "Productivity social media app" to Pair(100, 30),
                "Free Entertainment" to Pair(80, 0), "Paid Entertainment" to Pair(70, 0),
                "Shopping" to Pair(80, 0), "Grocery and Food" to Pair(90, 0), "Travel" to Pair(70, -10)),

            "Sales and Marketing" to mapOf("Influencers" to Pair(100, -10),
                "Messaging" to Pair(100, 0), "Productivity social media app" to Pair(90, -10),
                "Free Entertainment" to Pair(70, -10), "Paid Entertainment" to Pair(70, -20),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(90, -20), "Travel" to Pair(90, -10)),

            "Social Work" to mapOf("Influencers" to Pair(100, 40),
                "Messaging" to Pair(100, 40), "Productivity social media app" to Pair(100, 50),
                "Free Entertainment" to Pair(70, 0), "Paid Entertainment" to Pair(70, -10),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -40), "Travel" to Pair(90, -10)),

            "Sports and Athletics" to mapOf("Influencers" to Pair(90, -10),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, -30),
                "Free Entertainment" to Pair(80, -10), "Paid Entertainment" to Pair(80, -20),
                "Shopping" to Pair(90, 0), "Grocery and Food" to Pair(90, 0), "Travel" to Pair(100, 20)),

            "Driver" to mapOf("Influencers" to Pair(80, -30),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(70, -20), "Paid Entertainment" to Pair(60, -30),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(70, 0)),

            "Student" to mapOf("Influencers" to Pair(90, -15),
                "Messaging" to Pair(100, -20), "Productivity social media app" to Pair(100, 40),
                "Free Entertainment" to Pair(70, -15), "Paid Entertainment" to Pair(80, -20),
                "Shopping" to Pair(90, -10), "Grocery and Food" to Pair(90, 10), "Travel" to Pair(90, 10)),

            "House Maker" to mapOf("Influencers" to Pair(90, 10),
                "Messaging" to Pair(100, 10), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(90, -10), "Paid Entertainment" to Pair(90, -20),
                "Shopping" to Pair(90, -10), "Grocery and Food" to Pair(90, -10), "Travel" to Pair(80, -20)),

            "Others" to mapOf("Influencers" to Pair(80, -30),
                "Messaging" to Pair(90, -20), "Productivity social media app" to Pair(100, 0),
                "Free Entertainment" to Pair(70, -20), "Paid Entertainment" to Pair(60, -30),
                "Shopping" to Pair(80, -10), "Grocery and Food" to Pair(80, -10), "Travel" to Pair(70, 0))
        )
    )
}