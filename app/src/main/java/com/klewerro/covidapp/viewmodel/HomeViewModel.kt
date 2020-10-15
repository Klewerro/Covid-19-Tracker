package com.klewerro.covidapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.klewerro.covidapp.api.CovidApi
import com.klewerro.covidapp.data.CovidRepository
import com.klewerro.covidapp.model.CountryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeViewModel : ViewModel() {

    private val repository = CovidRepository()

    var countryData: LiveData<CountryData>

    init {
        countryData = getCountryData("PL")
    }


    fun getCountryData(countryCode: String): LiveData<CountryData> {
        return liveData(Dispatchers.IO) {
            val data = repository.getCountryData("PL")
            val countryData = data.data
            emit(countryData)
        }
    }
}