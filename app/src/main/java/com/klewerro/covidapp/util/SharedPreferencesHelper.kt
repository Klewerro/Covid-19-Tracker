package com.klewerro.covidapp.util

interface SharedPreferencesHelper {
    fun saveFetchTime(timeInNanoseconds: Long)
    fun getFetchTime(): Long
    fun saveCountryId(countryId: Int)
    fun getCountryId(): Int
    fun saveWidgetCountry(widgetId: Int, countryCode: String)
    fun getWidgetCountry(widgetId: Int): String?
}