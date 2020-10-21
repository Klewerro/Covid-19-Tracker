package com.klewerro.covidapp.ui

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.math.roundToInt

class DateXAxisValueFormatter(private val values: List<Date>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val date = values[value.roundToInt()]
        val formatter = SimpleDateFormat("dd.MM")
        val formattedDate = formatter.format(date)
        return formattedDate
    }
}