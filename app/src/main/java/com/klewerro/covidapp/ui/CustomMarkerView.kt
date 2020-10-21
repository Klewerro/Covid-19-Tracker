package com.klewerro.covidapp.ui

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.klewerro.covidapp.R
import com.klewerro.covidapp.model.CountryData

class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private var tv: TextView = findViewById(R.id.markerContentTextView)


    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        tv.text = e!!.y.toInt().toString()

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat());
    }
}