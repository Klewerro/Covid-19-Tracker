package com.klewerro.covidapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class CountryData(
        val name: String,
        val code: String,
        val population: Long,
        @SerializedName("updated_at") val updatedAt: Date,
        @SerializedName("today") val todayStatistic: TodayStatistic,
        @SerializedName("latest_data") val latestData: LatestData,
        @SerializedName("timeline") val timelineData: List<TimelineData>
) {

    data class TodayStatistic(
            val deaths: Int,
            val confirmed: Int
    )

    data class LatestData(
            val deaths: Int,
            val confirmed: Int,
            val recovered: Int,
            val critical: Int,
            @SerializedName("calculated") val latestDataCalculated: LatestDataCalculated
    ) {

        data class LatestDataCalculated(
                @SerializedName("death_rate") val deathRate: Double?,
                @SerializedName("recovery_rate") val recoveryRate: Double?,
                @SerializedName("recovered_vs_death_ratio") val recoveredVsDeathRatio: Double?,
                @SerializedName("cases_per_million_population") val casesPerMillion: Int
        )
    }

    data class TimelineData(
            @SerializedName("updated_at") val updatedAt: Date,
            val date: Date,
            val deaths: Int,
            val confirmed: Int,
            val active: Int,
            val recovered: Int,
            @SerializedName("new_confirmed") val newConfirmed: Int,
            @SerializedName("new_recovered") val newRecovered: Int,
            @SerializedName("new_deaths") val newDeaths: Int,
            @SerializedName("is_in_progress") val isInProgress: Boolean,
            var id: Int
    )
}
