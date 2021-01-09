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

    val countryDataWithTimeline = repository.countryDataWithTimeline
    var _countries = repository.countries
    set(value) {
        if (selectedCountry.value == null) {

        }
    }

    val countries = _countries
    val selectedCountry = repository.country
    var dailyTimelineData = MutableLiveData<List<DailyTimelineData>>()

    fun getCountries() = viewModelScope.launch(Dispatchers.IO) {
        repository.getCountryList()
    }

    fun getSelectedCountry() = viewModelScope.launch(Dispatchers.IO) {
        repository.getCountry(sharedPreferencesHelper.getCountryId())
    }

    fun getCountryDataWithTimeline(countryCode: String) = viewModelScope.launch(Dispatchers.IO) {
            if (checkIsTimeForFetch()) {
                repository.getCountryData("PL")
                sharedPreferencesHelper.saveFetchTime(System.nanoTime())
                //showToast("Data from API.")   //Todo: add liveData variable and change state. Observe on fragment code
            } else {
                repository.getCountryDataOffline("PL")
                //showToast("Data from database.")
            }
        }

    //Todo: Get rid of it: data is already in new_... timelineData fields
    fun getDailyCasesFromTimelineData(timelineData: List<TimelineData>) {
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

    fun getCountryCode(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { position ->
            val countryCode = getCountryCodeFromLocation(context, Pair(position.latitude, position.longitude))

            viewModelScope.launch (Dispatchers.IO) {
                findCountryFromCode(countryCode)
            }
        }
    }

    fun getCountryCodeFromPhoneSettings(context: Context) {
        val locales = context.resources.configuration.locales
        val tag = locales[0].country
        viewModelScope.launch(Dispatchers.IO) {
            findCountryFromCode(tag)
        }
    }

    fun setSelectedCountry(countryIndex: Int) {
        val selectedCountry = countries.value?.get(countryIndex) ?: return
        if (selectedCountry.id != this.selectedCountry.value?.id) {
            sharedPreferencesHelper.saveCountryId(selectedCountry.id)
            this.selectedCountry.postValue(selectedCountry)
        }
    }


    private fun checkIsTimeForFetch(): Boolean {
        val currentTime = System.nanoTime()
        lastUpdateTime = sharedPreferencesHelper.getFetchTime()
        return currentTime - lastUpdateTime > refreshTime
    }

    private fun getCountryCodeFromLocation(context: Context, latLang: Pair<Double, Double>): String  {  //Todo: add position
        val myLocation = Geocoder(context)
        val list = myLocation.getFromLocation(latLang.first, latLang.second, 3)
        return list[0].countryCode
    }

    private suspend fun findCountryFromCode(countryCode: String) {
        repository.getCountry(countryCode)
        selectedCountry.value?.let { sharedPreferencesHelper.saveCountryId(it.id) }
    }
}