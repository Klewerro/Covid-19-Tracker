package com.klewerro.covidapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.klewerro.covidapp.data.entity.CountryData
import com.klewerro.covidapp.data.entity.CountryDataWithTimeline

@Dao
interface CountryDataDao {

    @Insert
    suspend fun insertCountryData(countryData: CountryData): Long

    @Transaction
    @Query("SELECT * FROM country_data")
    suspend fun getAllCountryData(): List<CountryDataWithTimeline>
}