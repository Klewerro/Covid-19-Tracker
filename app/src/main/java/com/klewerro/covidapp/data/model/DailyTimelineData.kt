package com.klewerro.covidapp.data.model

import java.util.*

data class DailyTimelineData(
    override var id: Int,

    override val date: Date,

    override val deaths: Int,

    override val confirmed: Int,

    override val active: Int,

    override val recovered: Int,
) : TimelineAbstract()