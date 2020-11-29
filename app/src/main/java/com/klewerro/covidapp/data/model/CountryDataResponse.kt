package com.klewerro.covidapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class CountryDataResponse(
    val id: Long,

    val name: String,

    val code: String,

    val population: Long,

    @SerializedName("updated_at")
    val updatedAt: Date,

    @SerializedName("today")
    val todayStatistic: CountryData.TodayStatistic,

    @SerializedName("latest_data")
    val latestData: CountryData.LatestData,

    @SerializedName("timeline")
    val timelineData: List<TimelineData>
)