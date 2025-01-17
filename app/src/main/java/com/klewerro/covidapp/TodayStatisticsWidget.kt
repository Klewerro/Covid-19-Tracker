package com.klewerro.covidapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import com.klewerro.covidapp.data.repository.CovidRepository
import com.klewerro.covidapp.data.repository.WidgetCovidRepository
import com.klewerro.covidapp.util.SharedPreferencesHelper
import com.klewerro.covidapp.util.formatToString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 */
@AndroidEntryPoint
class TodayStatisticsWidget : AppWidgetProvider() {

    @Inject lateinit var repository: WidgetCovidRepository
    @Inject lateinit var sharedPreferencesHelper: SharedPreferencesHelper

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

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)

        CoroutineScope(Dispatchers.IO).launch {
            val countryCode = sharedPreferencesHelper.getWidgetCountry(appWidgetId)
            if (countryCode != null) {
                val countryData = repository.getCountryDataOffline(countryCode).countryData
                val todayStatistics = countryData.todayStatistic

                updateAppWidget(
                    context, appWidgetManager, appWidgetId, countryData.name,
                    todayStatistics.confirmed, todayStatistics.deaths, countryData.updatedAt
                )
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        fun online() {
            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)
            Log.d(TAG, "onReceive called: widgetId: $appWidgetId")

            updateAppWidgetState(
                context,
                AppWidgetManager.getInstance(context),
                appWidgetId,
                WidgetState.REFRESH,
                true
            )

            performDataUpdate(context, AppWidgetManager.getInstance(context), appWidgetId)
        }

        fun offline() {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, this::class.java))

            CoroutineScope(Dispatchers.IO).launch {
                for (widgetId in appWidgetIds) {
                    updateAppWidgetState(
                        context,
                        AppWidgetManager.getInstance(context),
                        widgetId,
                        WidgetState.REFRESH,
                        false
                    )

                    performOfflineDataUpdate(context, appWidgetManager, widgetId)
                }
            }
        }

        when (intent?.action) {
            ACTION_REFRESH_WIDGET_DATA -> online()
            ACTION_REFRESH_WIDGETS_AFTER_IN_APP_DATA_FETCH -> offline()
        }
    }


    private fun performDataUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val countryCode = sharedPreferencesHelper.getWidgetCountry(appWidgetId)
            Log.d(TAG, "performDataUpdate country: $countryCode")
            if (countryCode != null) {
                val countryData = repository.getCountryData(countryCode).countryData
                val todayStatistics = countryData.todayStatistic

                updateAppWidget(
                    context, appWidgetManager, appWidgetId, countryData.name,
                    todayStatistics.confirmed, todayStatistics.deaths, countryData.updatedAt
                )
            }

        }
    }

    private suspend fun performOfflineDataUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val countryCode = sharedPreferencesHelper.getWidgetCountry(appWidgetId)
        Log.d(TAG, "performOfflineDataUpdate country: $countryCode")
        if (countryCode != null) {
            val countryData = repository.getCountryDataOffline(countryCode).countryData
            val todayStatistics = countryData.todayStatistic

            updateAppWidget(
                context, appWidgetManager, appWidgetId, countryData.name,
                todayStatistics.confirmed, todayStatistics.deaths, countryData.updatedAt
            )
        }
    }


    companion object {
        const val TAG = "TodayStatisticsWidget"
        const val ACTION_REFRESH_WIDGET_DATA = "action_refresh_intent_data"
        const val ACTION_REFRESH_WIDGETS_AFTER_IN_APP_DATA_FETCH = "action_refresh_all_widgets_after_in_app_fetch"
    }
}


internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    countryName: String,
    todayConfirmed: Int,
    todayDeaths: Int,
    updatedAt: Date
) {
    Log.d(TodayStatisticsWidget.TAG, "updateAppWidget called")
    val views = RemoteViews(context.packageName, R.layout.today_statistics_widget)
    val oneCellWidget = checkIfWidgetIsOneCell(context, appWidgetManager, appWidgetId)

    views.setTextViewText(R.id.countryCodeTextView, countryName)
    views.setTextViewText(
        R.id.confirmedTextView, if (!oneCellWidget)
            "${context.getString(R.string.confirmed)}: $todayConfirmed"
        else todayConfirmed.toString()
    )
    views.setTextViewText(
        R.id.deathsTextView,
        if (!oneCellWidget) "${context.getString(R.string.deaths)}: $todayDeaths"
        else todayDeaths.toString()
    )
    views.setTextViewText(R.id.updateDateTextView, updatedAt.formatToString("dd.MM - HH:mm"))

    views.setViewVisibility(R.id.stateTextView, View.GONE)
    views.setViewVisibility(R.id.dataWrapperRelativeLayout, View.VISIBLE)
    views.setViewVisibility(R.id.updateDateTextView, if (!oneCellWidget) View.VISIBLE else View.GONE)

    //After view click - open main application screen
    views.setOnClickPendingIntent(R.id.todayStatisticsWidget, getPendingIntentToActivity(context))

    //After button click, perform widget refresh
    val pendingIntentUpdate = getPendingIntentUpdateApp(context, appWidgetId)
    views.setOnClickPendingIntent(R.id.syncIcon, pendingIntentUpdate)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

internal fun updateAppWidgetState(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    state: WidgetState,
    showToast: Boolean
) {
    Log.d(TodayStatisticsWidget.TAG, "updateAppWidgetState called")

    val views = RemoteViews(context.packageName, R.layout.today_statistics_widget)
    views.setViewVisibility(R.id.dataWrapperRelativeLayout, View.GONE)
    views.setViewVisibility(R.id.stateTextView, View.VISIBLE)

    if (state == WidgetState.REFRESH) {
        views.setTextViewText(R.id.stateTextView, context.getString(R.string.refreshing) + "...")
        if (showToast) {
            Toast.makeText(
                context,
                context.getString(R.string.refreshing_widget) + "...",
                Toast.LENGTH_SHORT
            ).show()
        }

    } else if (state == WidgetState.ERROR) {
        Toast.makeText(
            context,
            context.getString(R.string.refreshing_widget_error) + "...",
            Toast.LENGTH_LONG
        ).show()
    }

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
        appWidgetId,
        intentUpdate,
        0
    )
}

private fun checkIfWidgetIsOneCell(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
): Boolean {
    val widgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId)
    val minWidth = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
    val maxWidth = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
    val oneCellWidth = convertDpToPx(40f, context).toInt() - 30
    val isOneCellWidth = oneCellWidth == 70
    Log.d(TodayStatisticsWidget.TAG, "updateAppWidget: min:$minWidth; max:$maxWidth")
    Log.d(
        TodayStatisticsWidget.TAG,
        "updateAppWidget: oneCellWidth:$oneCellWidth; isOneCellWidth:$isOneCellWidth"
    )

    if (oneCellWidth in minWidth..maxWidth) {
        Log.d(TodayStatisticsWidget.TAG, "updateAppWidget: in range = true")
        return true
    }

    return false
}

private fun convertDpToPx(sp: Float, context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        sp,
        context.resources.displayMetrics
    )
}

enum class WidgetState {
    REFRESH,
    ERROR
}