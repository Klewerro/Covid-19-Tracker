package com.klewerro.covidapp.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.klewerro.covidapp.R
import com.klewerro.covidapp.data.entity.TimelineData
import com.klewerro.covidapp.util.formatToString
import kotlinx.android.synthetic.main.details_recycler_item.view.itemActiveTextView
import kotlinx.android.synthetic.main.details_recycler_item.view.itemConfirmedTextView
import kotlinx.android.synthetic.main.details_recycler_item.view.itemDateTextView
import kotlinx.android.synthetic.main.details_recycler_item.view.itemDeathsTextView
import kotlinx.android.synthetic.main.details_recycler_item.view.itemNewActiveTextView
import kotlinx.android.synthetic.main.details_recycler_item.view.itemNewConfirmedTextView
import kotlinx.android.synthetic.main.details_recycler_item.view.itemNewDeathsTextView
import kotlinx.android.synthetic.main.details_recycler_item.view.itemNewRecoveredTextView
import kotlinx.android.synthetic.main.details_recycler_item.view.itemRecoveredTextView
import kotlinx.android.synthetic.main.details_recycler_item_horizontal.view.*


class TimelineDataAdapter(private val timelineDataList: List<TimelineData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_LEGEND) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.details_recycler_legend_horizontal, parent, false)
            LegendViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.details_recycler_item_horizontal, parent, false)
            TimelineDataViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position > 0)
            (holder as TimelineDataViewHolder).bind(position)
    }

    override fun getItemCount(): Int = timelineDataList.size

    override fun getItemViewType(position: Int): Int =
        if (position == 0) VIEW_TYPE_LEGEND else VIEW_TYPE_DATA


    class LegendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    inner class TimelineDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val confirmedTextView: TextView = itemView.itemConfirmedTextView
        private val deathsTextView: TextView = itemView.itemDeathsTextView
        private val activeTextView: TextView = itemView.itemActiveTextView
        private val recoveredTextView: TextView = itemView.itemRecoveredTextView
        private val newConfirmedTextView: TextView = itemView.itemNewConfirmedTextView
        private val newDeathsTextView: TextView = itemView.itemNewDeathsTextView
        private val newActiveTextView: TextView = itemView.itemNewActiveTextView
        private val newRecoveredTextView: TextView = itemView.itemNewRecoveredTextView
        private val dateTextView: TextView = itemView.itemDateTextView
        private val itemIsInProgressImageView: ImageView = itemView.itemIsInProgressImageView

        fun bind(position: Int) {
            val currentItem = timelineDataList[position]
            confirmedTextView.text = currentItem.confirmed.toString()
            deathsTextView.text = currentItem.deaths.toString()
            activeTextView.text = currentItem.active.toString()
            recoveredTextView.text = currentItem.recovered.toString()
            newConfirmedTextView.text = currentItem.newConfirmed.toString()
            newDeathsTextView.text = currentItem.newDeaths.toString()
            newRecoveredTextView.text = currentItem.newRecovered.toString()
            dateTextView.text = currentItem.date.formatToString("dd.MM.\nyyyy")
            itemIsInProgressImageView.visibility =
                if (currentItem.active == 1) View.VISIBLE else View.INVISIBLE
        }
    }

    companion object {
        private const val VIEW_TYPE_LEGEND = 1
        private const val VIEW_TYPE_DATA = 2
    }
}