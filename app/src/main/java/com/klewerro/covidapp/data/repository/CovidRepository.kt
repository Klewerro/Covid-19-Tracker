package com.klewerro.covidapp.data.repository

import com.klewerro.covidapp.data.entity.Country
import com.klewerro.covidapp.data.entity.CountryDataWithTimeline

interface CovidRepository {
    suspend fun getCountryData(countryCode: String): CountryDataWithTimeline
    suspend fun getCountryDataOffline(countryCode: String): List<CountryDataWithTimeline>
    suspend fun getCountryList(): List<Country>
    suspend fun getCountry(countryCode: String): Country
    suspend fun getCountry(id: Int): Country
}