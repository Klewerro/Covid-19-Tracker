package com.klewerro.covidapp.api

import androidx.lifecycle.LiveData
import com.klewerro.covidapp.model.CountryData

data class CoronaResponse(
        val data: CountryData
)