package com.screentimex.nouzze.Services

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.screentimex.nouzze.models.Constants
import com.screentimex.nouzze.models.ProductDetails

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.SHAREDPREFERENCE, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveDataObject(key: String, data: ProductDetails) {
        val json = gson.toJson(data)
        val editor = sharedPreferences.edit()
        editor.putString(key, json)
        editor.apply()
    }

    fun getDataObject(key: String): ProductDetails? {
        val json = sharedPreferences.getString(key, null)
        return gson.fromJson(json, ProductDetails::class.java)
    }

    fun clearPreference() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}