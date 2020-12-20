package com.klewerro.covidapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.klewerro.covidapp.data.repository.CovidRepository
import com.klewerro.covidapp.util.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 */
@AndroidEntryPoint
class TodayStatisticsWidget : AppWidgetProvider() {

    @Inject lateinit var repository: CovidRepository

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate called")
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            performDataUpdate(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == ACTION_REFRESH_WIDGET_DATA) {
            Log.d(TAG, "onReceive called")
            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)

            performDataUpdate(context, AppWidgetManager.getInstance(context), appWidgetId)
        }
    }


    private fun performDataUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val todayStatistics = repository.getCountryData("PL").countryData.todayStatistic
            updateAppWidget(context, appWidgetManager, appWidgetId,
                todayStatistics.confirmed, todayStatistics.deaths)
        }
    }


    companion object {
        const val ACTION_REFRESH_WIDGET_DATA = "action_refresh_intent_data"
        const val TAG = "TodayStatisticsWidget"
    }
}


internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    todayConfirmed: Int,
    todayDeaths: Int
) {
    Log.d(TodayStatisticsWidget.TAG, "updateAppWidget called")

    val views = RemoteViews(context.packageName, R.layout.today_statistics_widget)
    views.setTextViewText(R.id.confirmedTextView, "confirmed: $todayConfirmed")
    views.setTextViewText(R.id.deathsTextView, "deaths: $todayDeaths")


    //After view click - open main application screen
    views.setOnClickPendingIntent(R.id.todayStatisticsWidget, getPendingIntentToActivity(context))

    //After button click, perform widget refresh
    val pendingIntentUpdate = getPendingIntentUpdateApp(context, appWidgetId)
    views.setOnClickPendingIntent(R.id.syncIcon, pendingIntentUpdate)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}


private fun getPendingIntentToActivity(context: Context): PendingIntent {
    val intent = Intent(context, MainActivity::class.java)
    return PendingIntent.getActivity(context, 0, intent, 0)
}

private fun getPendingIntentUpdateApp(context: Context, appWidgetId: Int): PendingIntent {
    val intentUpdate = Intent(context, TodayStatisticsWidget::class.java)
    intentUpdate.action = TodayStatisticsWidget.ACTION_REFRESH_WIDGET_DATA
    intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    return PendingIntent.getBroadcast(
        context,
        0,
        intentUpdate,
        0
    )
}