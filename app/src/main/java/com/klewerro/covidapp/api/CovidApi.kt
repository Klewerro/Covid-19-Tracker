package com.klewerro.covidapp.api

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidApi {

    @GET("countries/{country_code}")
    suspend fun getCountryData(@Path("country_code") countryCode: String): CoronaResponse

    @GET("countries")
    suspend fun getCountryList(): CountryListResponse

    companion object {
        const val BASE_URL = "https://corona-api.com/"
    }
}