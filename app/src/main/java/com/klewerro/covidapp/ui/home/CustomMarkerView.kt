package com.klewerro.covidapp.ui.home

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.klewerro.covidapp.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class CustomMarkerView(context: Context, layoutResource: Int, private val dates: List<Date?>) :
    MarkerView(context, layoutResource) {

    private var markerValueTextView: TextView = findViewById(R.id.markerValueTextView)
    private var markerDateTextView: TextView = findViewById(R.id.markerDateTextView)


    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        markerValueTextView.text = e!!.y.toInt().toString()

        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val date = dates[e!!.x.roundToInt()]
        val formattedDate = formatter.format(date)
        markerDateTextView.text = formattedDate
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat() * 1.5f)
    }
}