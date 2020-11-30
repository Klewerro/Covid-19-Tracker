package com.klewerro.covidapp.ui

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.klewerro.covidapp.R
import com.klewerro.covidapp.data.model.DailyTimelineData
import com.klewerro.covidapp.data.model.TimelineAbstract
import com.klewerro.covidapp.data.model.TimelineData
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class CoronaLineChart(context: Context, attrs: AttributeSet) : LineChart(context, attrs) {

    init {
        //data = lineData
        description.isEnabled = false
        legend.isEnabled = false
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.setDrawGridLines(false)
        axisLeft.isEnabled = false
        axisRight.yOffset = -6f
        axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        //xAxis.granularity = 10f
        isDoubleTapToZoomEnabled = false
        setDrawGridBackground(false)
        //setVisibleXRangeMinimum(6f)
        axisLeft.axisMinimum = 0f
        axisRight.axisMinimum = 0f
        axisRight.isDrawBottomYLabelEntryEnabled

        // set onClick marker
        setupOnClickMarker()
    }

    fun setLineDataSet(timelineDataList: List<TimelineAbstract>) {
        addIdToTimelineData(timelineDataList)
        val entries = timelineDataList.map { Entry(it.id.toFloat(), it.confirmed.toFloat()) }
        val lineDataSet1 = LineDataSet(entries, "Confirmed cases")  // it.confirmed
        lineDataSet1.setDrawFilled(true)
        lineDataSet1.fillDrawable = ContextCompat.getDrawable(context, R.drawable.graph_line_gradient)
        lineDataSet1.color = ContextCompat.getColor(context, R.color.design_default_color_primary)
        lineDataSet1.circleHoleColor = ContextCompat.getColor(context, R.color.white)
        lineDataSet1.setCircleColor(R.color.design_default_color_primary)
        lineDataSet1.lineWidth = 4f
        lineDataSet1.highLightColor = ContextCompat.getColor(context, R.color.design_default_color_primary)
        lineDataSet1.setDrawHorizontalHighlightIndicator(false)
        lineDataSet1.setDrawValues(false)

        val lineData = LineData(lineDataSet1)
        val dates = timelineDataList.map { it.date }

        this.data = lineData
        this.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val date = dates[value.roundToInt()]
                val formatter = SimpleDateFormat("dd.MM")
                return formatter.format(date)
            }
        }

        this.axisRight.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if (value == 0f)
                    return ""

                return NumberFormat.getNumberInstance(Locale.US).format(value)
            }
        }

        invalidate()
    }


    private fun setupOnClickMarker() {
        val mv = CustomMarkerView(context, R.layout.custom_marker_view)
        mv.chartView = this
        this.marker = mv
    }

    private fun addIdToTimelineData(timelineData: List<TimelineAbstract>) {
        for ((i, data) in timelineData.withIndex()) {
            data.id = i
        }
    }
}