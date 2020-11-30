package com.klewerro.covidapp.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "country_data")
data class CountryData(
        @PrimaryKey(autoGenerate = true)
        override val id: Long,

        override val name: String,

        override val code: String,

        override val population: Long,

        @SerializedName("updated_at")
        @ColumnInfo(name = "updated_at")
        override val updatedAt: Date,

        @SerializedName("today")
        @Embedded(prefix = "today_")
        override val todayStatistic: TodayStatistic,

        @SerializedName("latest_data")
        @Embedded(prefix = "latest_")
        override val latestData: LatestData,
) : CountryDataAbstract() {
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

        override val date: Date,

        override val deaths: Int,

        override val confirmed: Int,

        override val active: Int,

        override val recovered: Int,

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

        override var id: Int
) : TimelineAbstract()