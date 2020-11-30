package com.klewerro.covidapp.di

import android.content.Context
import androidx.room.Room
import com.klewerro.covidapp.api.CovidApi
import com.klewerro.covidapp.data.database.CovidDatabase
import com.klewerro.covidapp.data.database.dao.CountryDataDao
import com.klewerro.covidapp.data.database.dao.TimelineDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(CovidApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideCovidApi(retrofit: Retrofit): CovidApi = retrofit.create(CovidApi::class.java)

    @Provides
    @Singleton
    fun provideCovidDatabase(@ApplicationContext appContext: Context): CovidDatabase = Room.databaseBuilder(
        appContext,
        CovidDatabase::class.java,
        "covid_database"
    ).build()

    @Provides
    fun provideCountryDataDao(covidDatabase: CovidDatabase): CountryDataDao = covidDatabase.countryDataDao()

    @Provides
    fun provideTimelineDataDao(covidDatabase: CovidDatabase): TimelineDataDao = covidDatabase.timelineDataDao()
}