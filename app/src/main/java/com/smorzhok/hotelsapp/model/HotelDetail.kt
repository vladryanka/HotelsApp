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
@Entity(tableName = "hotels_detail")
data class HotelDetail(
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
    @ColumnInfo(name = "image")
    val image: String?,
    @SerialName("suites_availability")
    @ColumnInfo(name = "suites_availability")
    val suitesAvailability: String,
    @ColumnInfo(name = "lat")
    val lat: Double,
    @ColumnInfo(name = "lon")
    val lon: Double
) : Parcelable