package com.klewerro.covidapp.data.repository

import android.content.Context
import com.klewerro.covidapp.api.CovidApi
import com.klewerro.covidapp.data.database.CovidDatabase
import com.klewerro.covidapp.data.model.CountryData
import com.klewerro.covidapp.data.model.CountryDataWithTimeline

class CovidRepository(context: Context) {

    private val covidApi = CovidApi.getCovidApi()
    private val countryDataDao = CovidDatabase.getDatabase(context).countryDataDao()
    private val timelineDataDao = CovidDatabase.getDatabase(context).timelineDataDao()

    suspend fun getCountryData(countryCode: String): CountryDataWithTimeline {
        val response = covidApi.getCountryData(countryCode);
        val countryData = CountryData(response.data)

        val countryDataWithTimeline = CountryDataWithTimeline(countryData, response.data.timelineData)

        val countryDataId = countryDataDao.insertCountryData(countryDataWithTimeline.countryData)
        countryDataWithTimeline.timelineData.map { it.countryDataId = countryDataId }
        timelineDataDao.insertTimelineData(countryDataWithTimeline.timelineData)

        return countryDataWithTimeline
    }

    suspend fun getCountryDataOffline(): List<CountryDataWithTimeline>
        = countryDataDao.getAllCountryData();
}