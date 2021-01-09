package com.klewerro.covidapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.klewerro.covidapp.data.entity.Country
import com.klewerro.covidapp.data.repository.CovidRepository
import com.klewerro.covidapp.util.SharedPreferencesHelper

class TodayStatisticsWidgetConfigurationViewModel @ViewModelInject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val repository: CovidRepository
    ) : ViewModel() {

    private lateinit var selectedCountry: Country
    val countries: LiveData<List<Country>> = repository.countries


    fun setSelectedCountry(index: Int) {
        selectedCountry = countries.value?.get(index) ?: return
    }

    fun saveWidgetCountry(appWidgetId: Int): Boolean {
        val country = selectedCountry
        if (country?.code == null)
            return false

        val countryCode = country.code
        sharedPreferencesHelper.saveWidgetCountry(appWidgetId, countryCode)
        return true
    }
}