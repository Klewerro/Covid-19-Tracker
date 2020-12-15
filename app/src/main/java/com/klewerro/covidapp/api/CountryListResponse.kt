package com.klewerro.covidapp.api

import com.klewerro.covidapp.data.entity.Country

data class CountryListResponse(
    val data: List<Country>
)