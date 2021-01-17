package com.klewerro.covidapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.klewerro.covidapp.data.repository.CovidRepository
import com.klewerro.covidapp.util.SharedPreferencesHelper

class DetailsViewModel @ViewModelInject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    repository: CovidRepository
) : ViewModel() {

    private val _isAutoScrollEnabled = MutableLiveData(sharedPreferencesHelper.getDetailsAutoScroll())

    val countryDataWithTimeline = repository.countryDataWithTimeline
    val isAutoScrollEnabled: LiveData<Boolean> = _isAutoScrollEnabled


    fun getChartSelectedTimelineDataIndex(xPosition: Int): Int? {
        return countryDataWithTimeline.value?.let {
            val timelineData = it.timelineData[xPosition]
            timelineData.id

            val nOfElements = it.timelineData.size
            nOfElements - xPosition
        }
    }

    fun setAutoScroll(value: Boolean) {
        sharedPreferencesHelper.saveDetailsAutoScroll(value)
        _isAutoScrollEnabled.postValue(value)
    }

}