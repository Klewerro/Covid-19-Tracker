package com.klewerro.covidapp.api

import com.klewerro.covidapp.data.model.Country

data class CountryListResponse(
    val data: List<Country>
)