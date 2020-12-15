package com.klewerro.covidapp.data.model

import com.google.gson.annotations.SerializedName
import com.klewerro.covidapp.data.entity.CountryData
import com.klewerro.covidapp.data.entity.TimelineData
import java.util.*

data class CountryDataResponse(
    override val id: Long,

    override val name: String,

    override val code: String,

    override val population: Long,

    @SerializedName("updated_at")
    override val updatedAt: Date,

    @SerializedName("today")
    override val todayStatistic: CountryData.TodayStatistic,

    @SerializedName("latest_data")
    override val latestData: CountryData.LatestData,

    @SerializedName("timeline")
    val timelineData: List<TimelineData>
) : CountryDataAbstract()