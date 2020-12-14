package com.klewerro.covidapp.data.repository

import com.klewerro.covidapp.api.CovidApi
import com.klewerro.covidapp.data.database.dao.CountryDao
import com.klewerro.covidapp.data.database.dao.CountryDataDao
import com.klewerro.covidapp.data.database.dao.TimelineDataDao
import com.klewerro.covidapp.data.model.Country
import com.klewerro.covidapp.data.model.CountryData
import com.klewerro.covidapp.data.model.CountryDataWithTimeline
import javax.inject.Inject

class CovidRepositoryImpl @Inject constructor(
    private val covidApi: CovidApi,
    private val countryDataDao: CountryDataDao,
    private val timelineDataDao: TimelineDataDao,
    private val countryDao: CountryDao
    ) : CovidRepository {

    override suspend fun getCountryData(countryCode: String): CountryDataWithTimeline {
        val response = covidApi.getCountryData(countryCode);
        val countryData = CountryData(response.data)

        val countryDataWithTimeline = CountryDataWithTimeline(countryData, response.data.timelineData)

        val countryDataId = countryDataDao.insertCountryData(countryDataWithTimeline.countryData)
        countryDataWithTimeline.timelineData.map { it.countryDataId = countryDataId }
        timelineDataDao.insertTimelineData(countryDataWithTimeline.timelineData)

        return countryDataWithTimeline
    }

    override suspend fun getCountryDataOffline(): List<CountryDataWithTimeline>
        = countryDataDao.getAllCountryData();

    override suspend fun getCountryList(): List<Country> {
        var countryList: List<Country>

        return if (countryDao.getCountries().isEmpty()) {
            val response = covidApi.getCountryList()
            countryList = response.data
            countryDao.insertCountries(countryList)
            countryList
        } else {
            countryList = countryDao.getCountries()
            countryList
        }
    }

    override suspend fun getCountry(countryCode: String): Country =
        countryDao.getCountry(countryCode)

    override suspend fun getCountry(id: Int): Country =
        countryDao.getCountry(id)
}