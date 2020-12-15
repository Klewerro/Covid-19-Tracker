package com.klewerro.covidapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.klewerro.covidapp.data.entity.Country

@Dao
interface CountryDao {

    @Insert
    suspend fun insertCountries(countries: List<Country>)

    @Query("SELECT * from country")
    suspend fun getCountries(): List<Country>

    @Query("Select * from country where code LIKE :countryCode")
    suspend fun getCountry(countryCode: String): Country

    @Query("Select * from country where id = :id")
    suspend fun getCountry(id: Int): Country
}