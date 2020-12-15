package com.klewerro.covidapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.klewerro.covidapp.data.database.converter.DateConverter
import com.klewerro.covidapp.data.database.dao.CountryDao
import com.klewerro.covidapp.data.database.dao.CountryDataDao
import com.klewerro.covidapp.data.database.dao.TimelineDataDao
import com.klewerro.covidapp.data.entity.Country
import com.klewerro.covidapp.data.entity.CountryData
import com.klewerro.covidapp.data.entity.TimelineData

@Database(entities = [CountryData::class, TimelineData::class, Country::class],
        version = 10,
        exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class CovidDatabase : RoomDatabase() {

    abstract fun countryDataDao(): CountryDataDao
    abstract fun timelineDataDao(): TimelineDataDao
    abstract fun countryDao(): CountryDao
}