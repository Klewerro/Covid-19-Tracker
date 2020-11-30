package com.klewerro.covidapp.data.model

import java.util.*

abstract class TimelineAbstract() {
    abstract var id: Int

    abstract val date: Date?

    abstract val deaths: Int

    abstract val confirmed: Int

    abstract val active: Int

    abstract val recovered: Int
}