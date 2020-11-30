package com.klewerro.covidapp.util

interface SharedPreferencesHelper {
    fun saveFetchTime(timeInNanoseconds: Long)
    fun getFetchTime(): Long
}