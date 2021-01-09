package com.klewerro.covidapp.data.repository

import com.klewerro.covidapp.api.CovidApi
import com.klewerro.covidapp.data.database.dao.CountryDataDao
import com.klewerro.covidapp.data.database.dao.TimelineDataDao
import com.klewerro.covidapp.data.entity.CountryData
import com.klewerro.covidapp.data.entity.CountryDataWithTimeline
import javax.inject.Inject

class WidgetCovidRepository @Inject constructor(
    private val covidApi: CovidApi,
    private val countryDataDao: CountryDataDao,
    private val timelineDataDao: TimelineDataDao
) {


    suspend fun getCountryDataOffline(countryCode: String): CountryDataWithTimeline =
        countryDataDao.getAllCountryData(countryCode).last()

    suspend fun getCountryData(countryCode: String): CountryDataWithTimeline {
        val response = covidApi.getCountryData(countryCode)
        val countryData = CountryData(response.data)

        val countryDataWithTimeline = CountryDataWithTimeline(countryData, response.data.timelineData)

        val countryDataId = countryDataDao.insertCountryData(countryDataWithTimeline.countryData)
        countryDataWithTimeline.timelineData.map { it.countryDataId = countryDataId }
        timelineDataDao.insertTimelineData(countryDataWithTimeline.timelineData)

        return countryDataWithTimeline
    }

}