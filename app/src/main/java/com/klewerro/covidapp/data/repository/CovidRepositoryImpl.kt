package com.klewerro.covidapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.klewerro.covidapp.api.CovidApi
import com.klewerro.covidapp.data.database.dao.CountryDao
import com.klewerro.covidapp.data.database.dao.CountryDataDao
import com.klewerro.covidapp.data.database.dao.TimelineDataDao
import com.klewerro.covidapp.data.entity.Country
import com.klewerro.covidapp.data.entity.CountryData
import com.klewerro.covidapp.data.entity.CountryDataWithTimeline
import kotlinx.coroutines.delay
import javax.inject.Inject

class CovidRepositoryImpl @Inject constructor(
    private val covidApi: CovidApi,
    private val countryDataDao: CountryDataDao,
    private val timelineDataDao: TimelineDataDao,
    private val countryDao: CountryDao
    ) : CovidRepository {

    override val countries = countryDao.getCountries()
    override val country = MutableLiveData<Country>()
    override val countryDataWithTimeline = MutableLiveData<CountryDataWithTimeline>()

    override suspend fun getCountryData(countryCode: String) {
        val response = covidApi.getCountryData(countryCode)
        val countryData = CountryData(response.data)

        val countryDataWithTimeline = CountryDataWithTimeline(countryData, response.data.timelineData)

        val countryDataId = countryDataDao.insertCountryData(countryDataWithTimeline.countryData)
        countryDataWithTimeline.timelineData.map { it.countryDataId = countryDataId }
        timelineDataDao.insertTimelineData(countryDataWithTimeline.timelineData)

        this.countryDataWithTimeline.postValue(countryDataWithTimeline)
    }

    override suspend fun getCountryDataOffline(countryCode: String) {
        this.countryDataWithTimeline.postValue(countryDataDao.getAllCountryData(countryCode).last())
    }

    override suspend fun getCountryList() {
        if (countries.value?.isEmpty() == true) {
            val response = covidApi.getCountryList()
            val countryList = response.data
            countryDao.insertCountries(countryList)
        }
    }

    override suspend fun getCountry(countryCode: String) {
        country.postValue(countryDao.getCountry(countryCode))
    }


    override suspend fun getCountry(id: Int) {
        if (countries.value?.isEmpty() == false) {
            country.postValue(countryDao.getCountry(id))
        } else {
            //For case, if selectedCountry will be loaded before countries
            delay(100)
            getCountry(id)
            Log.d("TodayStatisticsWidget", "awaited")
        }
    }

}