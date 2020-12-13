package com.klewerro.covidapp.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.klewerro.covidapp.R
import com.klewerro.covidapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
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

        viewModel.dailyTimelineData.observe(viewLifecycleOwner) {dailyTimelineData ->
            chartDaily.setLineDataSet(dailyTimelineData.reversed())
        }


        viewModel.countries.observe(viewLifecycleOwner) {countries ->
            countriesSpinner.adapter = ArrayAdapter(requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                countries.map { it.name })
        }
    }
}