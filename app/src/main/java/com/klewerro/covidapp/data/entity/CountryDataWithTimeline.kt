package com.klewerro.covidapp.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CountryDataWithTimeline(
    @Embedded
    val countryData: CountryData,

    @Relation(parentColumn = "id",
        entityColumn = "countryDataId",
        entity = TimelineData::class)
    val timelineData: List<TimelineData>
)