package com.klewerro.covidapp.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.utils.MPPointD
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.klewerro.covidapp.R
import com.klewerro.covidapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home) {


    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.countryDataWithTimeline.observe(viewLifecycleOwner) { countryData ->
            countryTextView.text = countryData.countryData.name
            populationSizeTextView.text = "Population: ${countryData.countryData.population}"
            confirmedTextView.text = "Confirmed: ${countryData.countryData.todayStatistic.confirmed}"
            deathsTextView.text = "Deaths: ${countryData.countryData.todayStatistic.deaths}"

            chart.setLineDataSet(countryData.timelineData.reversed())
        }
    }
}