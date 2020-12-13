package com.klewerro.covidapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country")
data class Country(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,

    val code: String
)