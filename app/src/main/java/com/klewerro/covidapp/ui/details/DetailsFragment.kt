package com.klewerro.covidapp.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.klewerro.covidapp.R
import com.klewerro.covidapp.data.entity.TimelineData
import com.klewerro.covidapp.util.setFragmentTitle
import com.klewerro.covidapp.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel by viewModels<DetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.countryDataWithTimeline.observe(viewLifecycleOwner) { countryDataWithTimeline ->
            setupRecyclerView(countryDataWithTimeline.timelineData)
            setFragmentTitle("${countryDataWithTimeline.countryData.name} ${requireContext().getString(R.string.details)}")
        }
    }


    private fun setupRecyclerView(timelineData: List<TimelineData>) {
        timelineRecyclerView.adapter = TimelineDataAdapter(timelineData)
        timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        timelineRecyclerView.setHasFixedSize(true)
    }
}