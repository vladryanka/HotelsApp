package com.smorzhok.hotelsapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "hotels")
data class Hotel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "stars")
    val stars: Float,
    @ColumnInfo(name = "distance")
    val distance: Float,
    @SerialName("suites_availability")
    @ColumnInfo(name = "suites_availability")
    val suitesAvailability: String
) : Parcelable