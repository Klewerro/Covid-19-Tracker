package com.klewerro.covidapp.data.repository

import com.klewerro.covidapp.data.model.Country
import com.klewerro.covidapp.data.model.CountryDataWithTimeline

interface CovidRepository {
    suspend fun getCountryData(countryCode: String): CountryDataWithTimeline
    suspend fun getCountryDataOffline(): List<CountryDataWithTimeline>
    suspend fun getCountryList(): List<Country>
    suspend fun getCountry(countryCode: String): Country
    suspend fun getCountry(id: Int): Country
}