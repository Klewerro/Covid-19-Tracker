package com.klewerro.covidapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

abstract class CountryDataAbstract() {
    abstract val id: Long

    abstract val name: String

    abstract val code: String

    abstract val population: Long

    abstract val updatedAt: Date

    abstract val todayStatistic: CountryData.TodayStatistic

    abstract val latestData: CountryData.LatestData
}