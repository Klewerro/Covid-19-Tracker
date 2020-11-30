package com.klewerro.covidapp.data.repository

import com.klewerro.covidapp.data.model.CountryDataWithTimeline

interface CovidRepository {
    suspend fun getCountryData(countryCode: String): CountryDataWithTimeline
    suspend fun getCountryDataOffline(): List<CountryDataWithTimeline>
}