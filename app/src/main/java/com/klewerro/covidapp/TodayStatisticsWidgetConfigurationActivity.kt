package com.klewerro.covidapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.klewerro.covidapp.util.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_today_statistics_widget_configuration.*
import javax.inject.Inject

@AndroidEntryPoint
class TodayStatisticsWidgetConfigurationActivity : AppCompatActivity() {

    @Inject lateinit var sharedPreferencesHelper: SharedPreferencesHelper

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

        //Necessary in some scenarios, some phones may need appWidgetId to proper close activity - just in case
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        this.setResult(RESULT_CANCELED, resultValue)

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
            finish()

        acceptButton.setOnClickListener {
            val countryCode = countryCodeTextView.text.toString()

            sharedPreferencesHelper.saveWidgetCountry(appWidgetId, countryCode)

            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            this.setResult(RESULT_OK, resultValue)
            finish()

            callIntentUpdateAppReceiver()
        }
    }

    private fun callIntentUpdateAppReceiver() {
        val intentUpdate = Intent(this, TodayStatisticsWidget::class.java)
        intentUpdate.action = TodayStatisticsWidget.ACTION_REFRESH_WIDGET_DATA
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        this.sendBroadcast(intentUpdate)
    }

    private fun getPendingIntentUpdateApp(context: Context, appWidgetId: Int): PendingIntent {
        val intentUpdate = Intent(context, TodayStatisticsWidget::class.java)
        intentUpdate.action = TodayStatisticsWidget.ACTION_REFRESH_WIDGET_DATA
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

        return PendingIntent.getBroadcast(
            context,
            appWidgetId,
            intentUpdate,
            0
        )
    }

}