package com.klewerro.covidapp.data

import androidx.lifecycle.LiveData
import com.klewerro.covidapp.api.CoronaResponse
import com.klewerro.covidapp.api.CovidApi
import kotlinx.coroutines.Deferred

class CovidRepository {

    private val covidApi = CovidApi.getCovidApi()

    suspend fun getCountryData(countryCode: String): CoronaResponse {
        return covidApi.getCountryData(countryCode)
    }
}