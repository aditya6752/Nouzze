package com.screentimex.nouzze.Services

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.UserDetails

class MidNightUsageStateSharedPref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.MID_NIGHT_USAGE_DATA_SHARED_PREFS, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveDataObject(key: String, data: UserDetails) {
        val json = gson.toJson(data)
        val editor = sharedPreferences.edit()
        editor.putString(key, json)
        editor.apply()
    }

    fun getDataObject(key: String): UserDetails {
        val json = sharedPreferences.getString(key, null)
        return gson.fromJson(json, UserDetails::class.java)
    }

    fun clearPreference() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun containsData(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
}