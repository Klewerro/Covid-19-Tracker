package com.klewerro.covidapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.klewerro.covidapp.data.entity.Country
import com.klewerro.covidapp.data.entity.CountryDataWithTimeline

interface CovidRepository {
    val countries: LiveData<List<Country>>
    val country: MutableLiveData<Country>
    val countryDataWithTimeline: MutableLiveData<CountryDataWithTimeline>


    suspend fun getCountryData(countryCode: String)
    suspend fun getCountryDataOffline(countryCode: String)
    suspend fun getCountryList()
    suspend fun getCountry(countryCode: String)
    suspend fun getCountry(id: Int)
}