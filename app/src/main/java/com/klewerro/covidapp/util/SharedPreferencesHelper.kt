package com.klewerro.covidapp.util

import android.content.Context
import android.content.SharedPreferences
import com.klewerro.covidapp.BuildConfig

class SharedPreferencesHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("default_preferences", Context.MODE_PRIVATE);


    fun saveFetchTime(timeInNanoseconds: Long) = prefs.edit()
        .putLong(PREF_FETCH_TIME, timeInNanoseconds)
        .apply()

    fun getFetchTime(): Long = prefs.getLong(PREF_FETCH_TIME, -1)


    companion object {
        private val PREF_FETCH_TIME = "${BuildConfig.APPLICATION_ID}_pref_time"
        private val instance: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            if (instance == null) {
                return SharedPreferencesHelper(context)
            }

            return instance
        }
    }
}