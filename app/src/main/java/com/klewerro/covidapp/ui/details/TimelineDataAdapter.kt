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
import kotlinx.android.synthetic.main.details_recycler_item.view.*
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


class TimelineDataAdapter(private val timelineDataList: List<TimelineData>) : RecyclerView.Adapter<TimelineDataAdapter.TimelineDataViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineDataViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.details_recycler_item_horizontal, parent, false)
        return TimelineDataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TimelineDataViewHolder, position: Int) {
        val currentItem = timelineDataList[position]
        holder.confirmedTextView.text = currentItem.confirmed.toString()
        holder.deathsTextView.text = currentItem.deaths.toString()
        holder.activeTextView.text = currentItem.active.toString()
        holder.recoveredTextView.text = currentItem.recovered.toString()
        holder.newConfirmedTextView.text = currentItem.newConfirmed.toString()
        holder.newDeathsTextView.text = currentItem.newDeaths.toString()
        holder.newActiveTextView.text = "---"
        holder.newRecoveredTextView.text = currentItem.newRecovered.toString()
        holder.dateTextView.text = currentItem.date.formatToString("dd.MM.\nyyyy")
        holder.itemIsInProgressImageView.visibility = if (currentItem.active == 1) View.VISIBLE else View.INVISIBLE
    }

    override fun getItemCount(): Int = timelineDataList.size


    class TimelineDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val confirmedTextView: TextView = itemView.itemConfirmedTextView
        val deathsTextView: TextView = itemView.itemDeathsTextView
        val activeTextView: TextView = itemView.itemActiveTextView
        val recoveredTextView: TextView = itemView.itemRecoveredTextView
        val newConfirmedTextView: TextView = itemView.itemNewConfirmedTextView
        val newDeathsTextView: TextView = itemView.itemNewDeathsTextView
        val newActiveTextView: TextView = itemView.itemNewActiveTextView
        val newRecoveredTextView: TextView = itemView.itemNewRecoveredTextView
        val dateTextView: TextView = itemView.itemDateTextView
        val itemIsInProgressImageView: ImageView = itemView.itemIsInProgressImageView

    }

}