package com.klewerro.covidapp.viewmodel

import android.app.Application
import android.content.Context
import android.location.Geocoder
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.klewerro.covidapp.data.entity.Country
import com.klewerro.covidapp.data.entity.CountryDataWithTimeline
import com.klewerro.covidapp.data.model.DailyTimelineData
import com.klewerro.covidapp.data.entity.TimelineData
import com.klewerro.covidapp.data.repository.CovidRepository
import com.klewerro.covidapp.util.SharedPreferencesHelper
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var countryDataWithTimeline: LiveData<CountryDataWithTimeline>
    var countries: LiveData<List<Country>>
    var dailyTimelineData: MutableLiveData<List<DailyTimelineData>>
    var country: MutableLiveData<Country>

    init {
        countryDataWithTimeline = getCountryDataWithTimeline("PL")
        countries = getCountryList()
        dailyTimelineData = MutableLiveData()
        country = MutableLiveData()
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
        val country = repository.getCountry(sharedPreferencesHelper.getCountryId());
        withContext(Dispatchers.Main) {
            this@HomeViewModel.country.value = country
        }
    }

    fun testCall(countryCode: String) {
        countryDataWithTimeline = getCountryDataWithTimeline(countryCode)
    }

    fun getCountryCode(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { position ->
            val countryCode = getCountryCodeFromLocation(context, Pair(position.latitude, position.longitude))

            viewModelScope.launch (Dispatchers.IO) {
                findCountryFromCode(countryCode)
            }
        }
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

    private fun getCountryCodeFromLocation(context: Context, latLang: Pair<Double, Double>): String  {  //Todo: add position
        val test = context.resources.configuration.locales

        val myLocation = Geocoder(context)
        val list = myLocation.getFromLocation(latLang.first, latLang.second, 3)
        return list[0].countryCode
    }

    private suspend fun findCountryFromCode(countryCode: String) {
        val country = repository.getCountry(countryCode)

        sharedPreferencesHelper.saveCountryId(country.id)
        withContext(Dispatchers.Main) {
            this@HomeViewModel.country.value = country
        }
    }
}