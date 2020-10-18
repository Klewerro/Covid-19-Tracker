package com.klewerro.covidapp.ui

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.math.roundToInt

class DateXAxisValueFormatter(private val values: List<Date>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return super.getFormattedValue(value)
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        //return super.getAxisLabel(value, axis)
        val date = values[value.roundToInt()]
        val formatter = SimpleDateFormat("dd.MM.yyy")
        val formattedDate = formatter.format(date)
        return formattedDate
    }
}