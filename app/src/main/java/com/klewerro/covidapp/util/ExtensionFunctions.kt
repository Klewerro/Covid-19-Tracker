package com.klewerro.covidapp.util

import androidx.fragment.app.Fragment
import com.klewerro.covidapp.MainActivity
import java.text.SimpleDateFormat
import java.util.*

fun Date.formatToString(pattern: String): String {
    val format = SimpleDateFormat(pattern)
    return format.format(this)
}

fun Fragment.setFragmentTitle(title: String) {
    (requireActivity() as MainActivity).supportActionBar?.title = title
}