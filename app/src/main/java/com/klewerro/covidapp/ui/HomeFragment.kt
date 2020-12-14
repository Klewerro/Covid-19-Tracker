package com.klewerro.covidapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
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
        setHasOptionsMenu(true)

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

        viewModel.countryCode.observe(viewLifecycleOwner) {countryCode ->
            Toast.makeText(requireContext(), countryCode, Toast.LENGTH_LONG).show()
        }

        viewModel.countries.observe(viewLifecycleOwner) {countries ->
            countriesSpinner.adapter = ArrayAdapter(requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                countries.map { it.name })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_location) {
            checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION) { viewModel.getCountryCode(requireContext()) }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.getCountryCode(requireContext())
        }
    }


    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 101
    }
}