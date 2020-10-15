package com.klewerro.covidapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class CountryData(
        val name: String,
        val code: String,
        val population: Long,
        @SerializedName("updated_at") val updatedAt: Date,
        //@SerializedName("today") val todayStatistic: TodayStatistic,
        //@SerializedName("latest_data") val latestData: LatestData
) {

    data class TodayStatistic(
            val deaths: Int,
            val confirmed: Int
    )

    data class LatestData(
            val deaths: Int,
            val confirmed: Int,
            val recovered: Int,
            val critical: Int
    )
}
