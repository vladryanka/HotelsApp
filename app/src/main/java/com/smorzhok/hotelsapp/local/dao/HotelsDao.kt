package com.smorzhok.hotelsapp.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.smorzhok.hotelsapp.model.Hotel
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelsDao {
    @Query("SELECT * FROM hotels")
    fun getHotels(): Flow<List<Hotel>>

    @Insert
    fun add(hotel: Hotel)

    @Query("SELECT * FROM hotels WHERE id LIKE :id")
    fun getHotelById(id: Int): Hotel?

    @Update
    suspend fun update(hotel: Hotel)

}