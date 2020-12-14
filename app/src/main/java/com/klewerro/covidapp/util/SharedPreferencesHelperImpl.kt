package com.klewerro.covidapp.util

import android.content.Context
import android.content.SharedPreferences
import com.klewerro.covidapp.BuildConfig
import javax.inject.Inject

class SharedPreferencesHelperImpl @Inject constructor(context: Context) : SharedPreferencesHelper {
    private val prefs: SharedPreferences = context
        .getSharedPreferences("default_preferences", Context.MODE_PRIVATE)


    override fun saveFetchTime(timeInNanoseconds: Long) = prefs.edit()
        .putLong(PREF_FETCH_TIME, timeInNanoseconds)
        .apply()

    override fun getFetchTime() = prefs.getLong(PREF_FETCH_TIME, -1)

    override fun saveCountryId(countryId: Int) = prefs.edit()
        .putInt(PREF_COUNTRY_ID, countryId)
        .apply()

    override fun getCountryId() = prefs.getInt(PREF_COUNTRY_ID, -1)


    companion object {
        private const val PREF_FETCH_TIME = "${BuildConfig.APPLICATION_ID}_pref_time"
        private const val PREF_COUNTRY_ID = "${BuildConfig.APPLICATION_ID}_pref_country_id"
    }
}