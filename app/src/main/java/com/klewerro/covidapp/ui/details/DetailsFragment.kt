package com.klewerro.covidapp.ui.details

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.klewerro.covidapp.R
import com.klewerro.covidapp.data.entity.TimelineData
import com.klewerro.covidapp.util.MetricsUtil
import com.klewerro.covidapp.util.setFragmentTitle
import com.klewerro.covidapp.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.chart
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupChartNavigationTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        timelineRecyclerView.animate().translationY(0f)

        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if(viewModel.isAutoScrollEnabled.value == false)
                    return

                if (e != null) {
                    val index = viewModel.getChartSelectedTimelineDataIndex(e.x.toInt()) ?: return
                    val pixels = MetricsUtil.convertDpToPixel(65F, requireContext())
                    (timelineRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(index, pixels.roundToInt())
                }
            }

            override fun onNothingSelected() {
            }
        })

        viewModel.countryDataWithTimeline.observe(viewLifecycleOwner) { countryDataWithTimeline ->
            setupRecyclerView(countryDataWithTimeline.timelineData)
            setFragmentTitle(requireContext().getString(R.string.details, countryDataWithTimeline.countryData.name))
            chart.setLineDataSet(countryDataWithTimeline.timelineData.reversed())
        }
    }

    override fun onStop() {
        super.onStop()
        timelineRecyclerView.animate().translationY(400f)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)

        val autoScrollMenuItem = menu.getItem(0)
        viewModel.isAutoScrollEnabled.observe(viewLifecycleOwner) {
            autoScrollMenuItem.isChecked = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_auto_scroll -> {
                viewModel.setAutoScroll(!item.isChecked)
            }
        }

        return super.onOptionsItemSelected(item)
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