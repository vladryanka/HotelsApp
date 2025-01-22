package com.smorzhok.hotelsapp.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.smorzhok.hotelsapp.model.HotelDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelsDetailDao {
    @Query("SELECT * FROM hotels_detail")
    fun getHotelDetail(): Flow<HotelDetail>

    @Insert
    fun add(hotel: HotelDetail)

    @Query("SELECT * FROM hotels_detail WHERE id LIKE :id")
    fun getHotelById(id: Int): HotelDetail?

    @Update
    suspend fun update(hotelDetail: HotelDetail)

}