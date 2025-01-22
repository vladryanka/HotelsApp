package com.smorzhok.hotelsapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smorzhok.hotelsapp.local.HotelsDatabase.Companion.DATABASE_VERSION
import com.smorzhok.hotelsapp.local.dao.HotelsDao
import com.smorzhok.hotelsapp.local.dao.HotelsDetailDao
import com.smorzhok.hotelsapp.model.Hotel
import com.smorzhok.hotelsapp.model.HotelDetail

@Database(
    entities = [Hotel::class, HotelDetail::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class HotelsDatabase : RoomDatabase() {

    abstract fun hotelsDao(): HotelsDao
    abstract fun hotelDetailDao(): HotelsDetailDao

    companion object {

        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "HotelsDatabase"

        fun getInstance(context: Context): HotelsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                HotelsDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}