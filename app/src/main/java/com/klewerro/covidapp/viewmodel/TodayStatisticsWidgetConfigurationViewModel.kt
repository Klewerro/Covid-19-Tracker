package com.klewerro.covidapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.klewerro.covidapp.data.entity.Country
import com.klewerro.covidapp.data.repository.CovidRepository
import com.klewerro.covidapp.util.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers

class TodayStatisticsWidgetConfigurationViewModel @ViewModelInject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val repository: CovidRepository
    ) : ViewModel() {

    val countries: LiveData<List<Country>> = getCountryList()
    val selectedCountry: MutableLiveData<Country> = MutableLiveData()


    fun setSelectedCountry(index: Int) {
        selectedCountry.value = countries.value?.get(index)
    }

    fun saveWidgetCountry(appWidgetId: Int): Boolean {
        val country = selectedCountry.value
        if (country?.code == null)
            return false

        val countryCode = country.code
        sharedPreferencesHelper.saveWidgetCountry(appWidgetId, countryCode)
        return true
    }


    private fun getCountryList() = liveData(Dispatchers.IO) {
        emit(repository.getCountryList())
    }
}