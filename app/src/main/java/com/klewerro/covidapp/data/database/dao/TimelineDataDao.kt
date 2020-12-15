package com.klewerro.covidapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.klewerro.covidapp.data.entity.TimelineData
@Dao
interface TimelineDataDao {

    @Insert
    suspend fun insertTimelineData(timelineData: List<TimelineData>)
}