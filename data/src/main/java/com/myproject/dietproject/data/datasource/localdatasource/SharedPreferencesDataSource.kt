package com.myproject.dietproject.data.datasource.localdatasource

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private lateinit var sharedPreferences: SharedPreferences

    fun setSharedPreferenceFileName(fileName: String) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    fun getSharedPreferenceValue(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun addSharedPreferenceValue(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun deleteSharedPreferenceValue(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

}