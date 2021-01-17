package com.klewerro.covidapp.util

interface SharedPreferencesHelper {
    fun saveFetchTime(timeInNanoseconds: Long)
    fun getFetchTime(): Long
    fun saveLastFetchedCountryId(countryId: Int)
    fun getLastFetchedCountryId(): Int
    fun saveCountryId(countryId: Int)
    fun getCountryId(): Int
    fun saveWidgetCountry(widgetId: Int, countryCode: String)
    fun getWidgetCountry(widgetId: Int): String?
    fun saveDetailsAutoScroll(value: Boolean)
    fun getDetailsAutoScroll(): Boolean
}