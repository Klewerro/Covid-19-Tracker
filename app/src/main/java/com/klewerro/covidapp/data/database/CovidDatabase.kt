package com.klewerro.covidapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.klewerro.covidapp.data.database.converter.DateConverter
import com.klewerro.covidapp.data.database.dao.CountryDataDao
import com.klewerro.covidapp.data.database.dao.TimelineDataDao
import com.klewerro.covidapp.data.model.CountryData
import com.klewerro.covidapp.data.model.TimelineData

@Database(entities = [CountryData::class, TimelineData::class],
        version = 10,
        exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class CovidDatabase : RoomDatabase() {

    abstract fun countryDataDao(): CountryDataDao
    abstract fun timelineDataDao(): TimelineDataDao


    companion object {
        @Volatile
        private var _instance: CovidDatabase? = null

        fun getDatabase(context: Context): CovidDatabase {
            val tempInstance = _instance
            tempInstance?.let {
                return it
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CovidDatabase::class.java,
                        "covid_database"
                ).build()

                _instance = instance
                return instance
            }

        }
    }

}