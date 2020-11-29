package com.klewerro.covidapp.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "country_data")
data class CountryData(
        @PrimaryKey(autoGenerate = true)
        val id: Long,

        val name: String,

        val code: String,

        val population: Long,

        @SerializedName("updated_at")
        @ColumnInfo(name = "updated_at")
        val updatedAt: Date,

        @SerializedName("today")
        @Embedded(prefix = "today_")
        val todayStatistic: TodayStatistic,

        @SerializedName("latest_data")
        @Embedded(prefix = "latest_")
        val latestData: LatestData,
        //@SerializedName("timelineData") val timelineData: List<TimelineData>?
) {
        constructor(countryDataResponse: CountryDataResponse) : this(
                countryDataResponse.id,
                countryDataResponse.name,
                countryDataResponse.code,
                countryDataResponse.population,
                countryDataResponse.updatedAt,
                countryDataResponse.todayStatistic,
                countryDataResponse.latestData
        )


    data class TodayStatistic(
            val deaths: Int,

            val confirmed: Int
    )

    data class LatestData(
            val deaths: Int,

            val confirmed: Int,

            val recovered: Int,

            val critical: Int,

            @SerializedName("calculated")
            @Embedded(prefix = "calculated_")
            val latestDataCalculated: LatestDataCalculated
    ) {

        data class LatestDataCalculated(
                @SerializedName("death_rate")
                @ColumnInfo(name = "death_rate")
                val deathRate: Double?,

                @SerializedName("recovery_rate")
                @ColumnInfo(name = "recovery_rate")
                val recoveryRate: Double?,

                @SerializedName("recovered_vs_death_ratio")
                @ColumnInfo(name = "recovered_vs_death_ratio")
                val recoveredVsDeathRatio: Double?,

                @SerializedName("cases_per_million_population")
                @ColumnInfo(name = "cases_per_million")
                val casesPerMillion: Int
        )
    }


}


@Entity(tableName = "timeline_data", foreignKeys = [ForeignKey(
        entity = CountryData::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("countryDataId"),
        onDelete = ForeignKey.CASCADE
)])
data class TimelineData(
        @PrimaryKey(autoGenerate = true)
        val idDb: Int,

        @SerializedName("updated_at")
        @ColumnInfo(name = "recovered_vs_death_ratio")
        val updatedAt: Date,

        val date: Date,

        val deaths: Int,

        val confirmed: Int,

        val active: Int,

        val recovered: Int,

        @SerializedName("new_confirmed")
        @ColumnInfo(name = "new_confirmed")
        val newConfirmed: Int,

        @SerializedName("new_recovered")
        @ColumnInfo(name = "new_recovered")
        val newRecovered: Int,

        @SerializedName("new_deaths")
        @ColumnInfo(name = "new_deaths")
        val newDeaths: Int,

        @SerializedName("is_in_progress")
        @ColumnInfo(name = "is_in_progress")
        val isInProgress: Boolean,

        var countryDataId: Long,

        var id: Int
)