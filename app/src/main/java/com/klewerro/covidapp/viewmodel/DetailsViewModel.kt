package com.klewerro.covidapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.klewerro.covidapp.data.repository.CovidRepository

class DetailsViewModel @ViewModelInject constructor(private val repository: CovidRepository) : ViewModel() {

    val countryDataWithTimeline = repository.countryDataWithTimeline


}