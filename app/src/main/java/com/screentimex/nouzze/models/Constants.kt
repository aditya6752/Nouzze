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

    fun getFileExtension(activity: Activity, uri: Uri?): String?{
        return MimeTypeMap.getSingleton().
        getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}