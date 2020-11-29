package com.klewerro.covidapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.klewerro.covidapp.data.repository.CovidRepository
import com.klewerro.covidapp.data.model.CountryDataWithTimeline
import com.klewerro.covidapp.util.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CovidRepository(application.baseContext)
    private val sharedPreferencesHelper = SharedPreferencesHelper.getInstance(application)
    private val refreshTime = 5 * 60 * 1000 * 1000 * 1000L  // 5 minutes
    private var lastUpdateTime: Long = 0

    var countryDataWithTimeline: LiveData<CountryDataWithTimeline>


    init {
        countryDataWithTimeline = getCountryDataWithTimeline("PL")
    }


    fun getCountryDataWithTimeline(countryCode: String): LiveData<CountryDataWithTimeline> = liveData(
        Dispatchers.IO
    ) {
        if (checkIsTimeForFetch()) {
            val data = repository.getCountryData("PL")
            sharedPreferencesHelper.saveFetchTime(System.nanoTime())
            emit(data)
            showToast("Data from API.")
        } else {
            val data = repository.getCountryDataOffline().last()
            emit(data)
            showToast("Data from database.")
        }
    }


    private fun checkIsTimeForFetch(): Boolean {
        val currentTime = System.nanoTime()
        lastUpdateTime = sharedPreferencesHelper.getFetchTime()
        return currentTime - lastUpdateTime > refreshTime
    }
}