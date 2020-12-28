package com.klewerro.covidapp.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatToString(pattern: String): String {
    val format = SimpleDateFormat(pattern)
    return format.format(this)
}