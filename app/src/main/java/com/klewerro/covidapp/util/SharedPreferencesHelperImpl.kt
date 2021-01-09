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

    override fun getCountryId() = prefs.getInt(PREF_COUNTRY_ID, 1)

    override fun saveWidgetCountry(widgetId: Int, countryCode: String) = prefs.edit()
        .putString(PREF_WIDGET_COUNTRY_CODE + widgetId, countryCode)
        .apply()

    override fun getWidgetCountry(widgetId: Int): String? = prefs.getString(PREF_WIDGET_COUNTRY_CODE + widgetId, null)


    companion object {
        private const val PREF_FETCH_TIME = "${BuildConfig.APPLICATION_ID}_pref_time"
        private const val PREF_COUNTRY_ID = "${BuildConfig.APPLICATION_ID}_pref_country_id"
        private const val PREF_WIDGET_COUNTRY_CODE = "${BuildConfig.APPLICATION_ID}_pref_widget_country_code_"
    }
}