package com.klewerro.covidapp.ui.Home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.klewerro.covidapp.R
import com.klewerro.covidapp.TodayStatisticsWidget
import com.klewerro.covidapp.ui.checkPermissions
import com.klewerro.covidapp.ui.showToast
import com.klewerro.covidapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {


    private val viewModel by viewModels<HomeViewModel>()
    private var isInitialSpinnerSelection = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setObservers()

        countriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isInitialSpinnerSelection)
                    viewModel.setSelectedCountry(position)
                isInitialSpinnerSelection = false
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_location -> checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION) {
                viewModel.getCountryCode(requireContext())
            }
            R.id.menu_location_settings -> viewModel.getCountryCodeFromPhoneSettings(requireContext())
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


    private fun setObservers() {
        viewModel.countryDataWithTimeline.observe(viewLifecycleOwner) { countryData ->
            countryTextView.text = countryData.countryData.name
            populationSizeTextView.text = "Population: ${countryData.countryData.population}"
            confirmedTextView.text = "Confirmed: ${countryData.countryData.todayStatistic.confirmed}"
            deathsTextView.text = "Deaths: ${countryData.countryData.todayStatistic.deaths}"
            chart.setLineDataSet(countryData.timelineData.reversed())

            viewModel.getDailyCasesFromTimelineData(countryData.timelineData)
            sendUpdateWidgetsBroadcast()
        }

        viewModel.dailyTimelineData.observe(viewLifecycleOwner) {dailyTimelineData ->
            chartDaily.setLineDataSet(dailyTimelineData.reversed())
        }

        viewModel.countries.observe(viewLifecycleOwner) {countries ->
            countriesSpinner.adapter = ArrayAdapter(requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                countries.map { it.name })
                viewModel.getSelectedCountry()
            if (countries.isEmpty()) {
                viewModel.getCountries()
            }
        }

        viewModel.selectedCountry.observe(viewLifecycleOwner) { country ->
            if (country != null) {
                val index = viewModel.countries.value?.indexOfFirst { it.id == country.id }
                if (index != null) {
                    countriesSpinner.setSelection(index)
                    viewModel.getCountryDataWithTimeline(country.code)
                    showToast("Determined country: ${country.name}")
                }
            }
        }
    }

    private fun sendUpdateWidgetsBroadcast() {
        val intentUpdate = Intent(context, TodayStatisticsWidget::class.java)
        intentUpdate.action = TodayStatisticsWidget.ACTION_REFRESH_WIDGETS_AFTER_IN_APP_DATA_FETCH

        requireContext().sendBroadcast(intentUpdate)
    }

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 101
    }
}