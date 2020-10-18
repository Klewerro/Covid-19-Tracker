package com.klewerro.covidapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.klewerro.covidapp.R
import com.klewerro.covidapp.model.CountryData
import com.klewerro.covidapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {


    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.countryData.observe(viewLifecycleOwner) { countryData ->
            countryTextView.text = countryData.name
            populationSizeTextView.text = "Population: ${countryData.population}"
            confirmedTextView.text = "Confirmed: ${countryData.todayStatistic.confirmed}"
            deathsTextView.text = "Deaths: ${countryData.todayStatistic.deaths}"

            prepareChart(countryData.timelineData)
        }
    }


    private fun prepareChart(timelineData: List<CountryData.TimelineData>) {
        val timelineDateReversed = timelineData.reversed()
        addIdToTimelineData(timelineDateReversed)
        val entries = timelineDateReversed.map { Entry(it.id.toFloat(), it.confirmed.toFloat())}

        val lineDataSet1 = LineDataSet(entries, "Confirmed cases")  // it.confirmed
        val lineData = LineData(lineDataSet1)

        val dates = timelineData.map { it.date }

        chart.apply {
            data = lineData
            setDrawGridBackground(false)
            xAxis.valueFormatter = DateXAxisValueFormatter(dates)
            isDoubleTapToZoomEnabled = false
            setVisibleXRangeMinimum(6f)
        }

        chart.invalidate()
    }

    private fun addIdToTimelineData(timelineData: List<CountryData.TimelineData>) {
        for ((i, data) in timelineData.withIndex()) {
            data.id = i
        }
    }
}