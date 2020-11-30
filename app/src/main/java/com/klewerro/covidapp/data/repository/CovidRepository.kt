package com.klewerro.covidapp.data.repository

import android.content.Context
import com.klewerro.covidapp.api.CovidApi
import com.klewerro.covidapp.data.database.CovidDatabase
import com.klewerro.covidapp.data.database.dao.CountryDataDao
import com.klewerro.covidapp.data.database.dao.TimelineDataDao
import com.klewerro.covidapp.data.model.CountryData
import com.klewerro.covidapp.data.model.CountryDataWithTimeline
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CovidRepository @Inject constructor(
    private val covidApi: CovidApi,
    private val countryDataDao: CountryDataDao,
    private val timelineDataDao: TimelineDataDao
    ) {

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