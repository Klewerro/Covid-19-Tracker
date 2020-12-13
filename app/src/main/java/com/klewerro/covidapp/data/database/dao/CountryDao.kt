package com.klewerro.covidapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.klewerro.covidapp.data.model.Country

@Dao
interface CountryDao {

    @Insert
    suspend fun insertCountries(countries: List<Country>)

    @Query("SELECT * from country")
    suspend fun getCountries(): List<Country>
}