package com.klewerro.covidapp.ui.details

import android.os.Bundle
import android.transition.TransitionInflater
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
import kotlinx.android.synthetic.main.fragment_details.chart

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupChartNavigationTransition()
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timelineRecyclerView.animate().translationY(0f)

        viewModel.countryDataWithTimeline.observe(viewLifecycleOwner) { countryDataWithTimeline ->
            setupRecyclerView(countryDataWithTimeline.timelineData)
            setFragmentTitle("${countryDataWithTimeline.countryData.name} ${requireContext().getString(R.string.details)}")
            chart.setLineDataSet(countryDataWithTimeline.timelineData.reversed())
        }
    }

    override fun onStop() {
        super.onStop()
        timelineRecyclerView.animate().translationY(400f)
    }


    private fun setupChartNavigationTransition() {
        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    private fun setupRecyclerView(timelineData: List<TimelineData>) {
        timelineRecyclerView.adapter = TimelineDataAdapter(timelineData)
        timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        timelineRecyclerView.setHasFixedSize(true)
    }
}