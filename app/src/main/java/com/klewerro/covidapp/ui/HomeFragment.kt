package com.klewerro.covidapp.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.MPPointD
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.klewerro.covidapp.R
import com.klewerro.covidapp.model.CountryData
import com.klewerro.covidapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home) {


    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var datePicker: MaterialDatePicker<androidx.core.util.Pair<Long, Long>>
    private lateinit var calendar: Calendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.countryData.observe(viewLifecycleOwner) { countryData ->
            countryTextView.text = countryData.name
            populationSizeTextView.text = "Population: ${countryData.population}"
            confirmedTextView.text = "Confirmed: ${countryData.todayStatistic.confirmed}"
            deathsTextView.text = "Deaths: ${countryData.todayStatistic.deaths}"

            chart.setLineDataSet(countryData.timelineData.reversed())

            countryTextView.setOnClickListener {
                datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
            }
            populationSizeTextView.setOnClickListener {
                calendar.timeInMillis = viewModel.dateFrom
                val date1: Date = calendar.time

                calendar.timeInMillis = viewModel.dateTo
                val date2: Date = calendar.time

                Toast.makeText(requireContext(), "From: $date1, Fo: $date2", Toast.LENGTH_LONG).show()
            }
        }
    }

}