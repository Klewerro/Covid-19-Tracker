package com.klewerro.covidapp.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.klewerro.covidapp.data.model.Country
import com.klewerro.covidapp.data.model.CountryDataWithTimeline
import com.klewerro.covidapp.data.model.DailyTimelineData
import com.klewerro.covidapp.data.model.TimelineData
import com.klewerro.covidapp.data.repository.CovidRepository
import com.klewerro.covidapp.util.SharedPreferencesHelper
import com.klewerro.covidapp.util.SharedPreferencesHelperImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class HomeViewModel @ViewModelInject constructor(
    private val repository: CovidRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    private val refreshTime = 5 * 60 * 1000 * 1000 * 1000L  // 5 minutes
    private var lastUpdateTime: Long = 0

    var countryDataWithTimeline: LiveData<CountryDataWithTimeline>
    var countries: LiveData<List<Country>>
    var dailyTimelineData: MutableLiveData<List<DailyTimelineData>>

    init {
        countryDataWithTimeline = getCountryDataWithTimeline("PL")
        countries = getCountryList()
        dailyTimelineData = MutableLiveData()
    }


    fun getCountryDataWithTimeline(countryCode: String): LiveData<CountryDataWithTimeline> =
        liveData(Dispatchers.IO) {
            var data: CountryDataWithTimeline

            if (checkIsTimeForFetch()) {
                data = repository.getCountryData("PL")
                sharedPreferencesHelper.saveFetchTime(System.nanoTime())
                emit(data)
                //showToast("Data from API.")   //Todo: add liveData variable and change state. Observe on fragment code
            } else {
                data = repository.getCountryDataOffline().last()
                emit(data)
                //showToast("Data from database.")
            }

            getDailyCasesFromTimelineData(data.timelineData)
        }



    fun getCountryList() = liveData<List<Country>>(Dispatchers.IO) {
        emit(repository.getCountryList())
    }

    private fun checkIsTimeForFetch(): Boolean {
        val currentTime = System.nanoTime()
        lastUpdateTime = sharedPreferencesHelper.getFetchTime()
        return currentTime - lastUpdateTime > refreshTime
    }

    private fun getDailyCasesFromTimelineData(timelineData: List<TimelineData>) {
        viewModelScope.launch(Dispatchers.Default) {
            val data = List(timelineData.size - 1) { index ->
                val day1 = timelineData[index]
                val day2 = timelineData[index + 1]

                return@List DailyTimelineData(
                    index + 1,
                    day1.date,
                    day1.deaths - day2.deaths,
                    day1.confirmed - day2.confirmed,
                    day1.active - day2.active,
                    day1.recovered - day2.recovered
                )
            }

            withContext(Dispatchers.Main) {
                dailyTimelineData.value = data
            }
        }
    }
}