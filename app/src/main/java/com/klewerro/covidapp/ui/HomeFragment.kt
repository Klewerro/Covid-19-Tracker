package com.klewerro.covidapp.ui

import android.os.Bundle
import android.view.GestureDetector
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
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
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

            prepareChart(countryData.timelineData)
            prepareDatePicker()

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


    private fun prepareChart(timelineData: List<CountryData.TimelineData>) {
        val timelineDateReversed = timelineData.reversed()
        addIdToTimelineData(timelineDateReversed)
        val entries = timelineDateReversed.map { Entry(it.id.toFloat(), it.confirmed.toFloat())}

        val lineDataSet1 = LineDataSet(entries, "Confirmed cases")  // it.confirmed
        val lineData = LineData(lineDataSet1)
        lineDataSet1.setDrawFilled(true)
        lineDataSet1.fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.graph_line_gradient)
        lineDataSet1.color = ContextCompat.getColor(requireContext(), R.color.design_default_color_primary)
        lineDataSet1.circleHoleColor = ContextCompat.getColor(requireContext(), R.color.white)
        lineDataSet1.setCircleColor(R.color.design_default_color_primary)
        lineDataSet1.lineWidth = 4f
        lineDataSet1.highLightColor = ContextCompat.getColor(requireContext(), R.color.teal_200)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawValues(false)

        val dates = timelineDateReversed.map { it.date }

        chart.apply {
            data = lineData
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.valueFormatter = DateXAxisValueFormatter(dates)
            xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
            xAxis.setDrawGridLines(false)
            axisLeft.isEnabled = false
            axisRight.yOffset = -6f
            axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            //xAxis.granularity = 10f
            isDoubleTapToZoomEnabled = false

            setDrawGridBackground(false)
            //setVisibleXRangeMinimum(6f)

        }

        val mv = CustomMarkerView(requireContext(), R.layout.custom_marker_view)
        mv.chartView = chart
        chart.marker = mv

        chart.invalidate()
    }

    private fun addIdToTimelineData(timelineData: List<CountryData.TimelineData>) {
        for ((i, data) in timelineData.withIndex()) {
            data.id = i
        }
    }


}