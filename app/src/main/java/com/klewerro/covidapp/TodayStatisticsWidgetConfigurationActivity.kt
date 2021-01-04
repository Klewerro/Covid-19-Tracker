package com.klewerro.covidapp

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.klewerro.covidapp.viewmodel.TodayStatisticsWidgetConfigurationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_today_statistics_widget_configuration.*

@AndroidEntryPoint
class TodayStatisticsWidgetConfigurationActivity : AppCompatActivity() {

    private val viewModel by viewModels<TodayStatisticsWidgetConfigurationViewModel>()

    var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_statistics_widget_configuration)

        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        setObservers()

        //Necessary in some scenarios, some phones may need appWidgetId to proper close activity - just in case
        var resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        this.setResult(RESULT_CANCELED, resultValue)

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
            finish()

        acceptButton.setOnClickListener {
            val saveResult = viewModel.saveWidgetCountry(appWidgetId)
            if (saveResult) {
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                this.setResult(RESULT_OK, resultValue)
                callIntentUpdateAppReceiver()
            }

            finish()
        }
    }

    private fun setObservers() {
        viewModel.countries.observe(this) { countries ->
            countriesSpinner.adapter = ArrayAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                countries.map { it.name })

            countriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.setSelectedCountry(position)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                }
            }
        }
    }

    private fun callIntentUpdateAppReceiver() {
        val intentUpdate = Intent(this, TodayStatisticsWidget::class.java)
        intentUpdate.action = TodayStatisticsWidget.ACTION_REFRESH_WIDGET_DATA
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        this.sendBroadcast(intentUpdate)
    }
}