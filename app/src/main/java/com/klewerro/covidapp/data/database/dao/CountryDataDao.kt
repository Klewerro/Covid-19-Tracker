package com.klewerro.covidapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.klewerro.covidapp.data.model.CountryData
import com.klewerro.covidapp.data.model.CountryDataWithTimeline

@Dao
interface CountryDataDao {

    @Insert
    suspend fun insertCountryData(countryData: CountryData): Long

    @Transaction
    @Query("SELECT * FROM country_data")
    suspend fun getAllCountryData(): List<CountryDataWithTimeline>
}