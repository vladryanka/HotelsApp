package com.smorzhok.hotelsapp.ui.hotelScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smorzhok.hotelsapp.local.HotelsDatabase
import com.smorzhok.hotelsapp.local.dao.HotelsDao
import com.smorzhok.hotelsapp.model.Hotel
import com.smorzhok.hotelsapp.remote.HotelApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HotelsScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val appDatabase = HotelsDatabase.getInstance(application)
    private val hotelDao: HotelsDao = HotelsDatabase.getInstance(application).hotelsDao()
    private val _hotels = MutableLiveData<List<Hotel>>()
    val hotels: LiveData<List<Hotel>> get() = _hotels


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadHotels() {
        viewModelScope.launch {

            _isLoading.value = true
            try {
                val response = HotelApi.retrofitService.loadHotels()
                _hotels.value = response
                val hotelsInDao = hotelDao.getHotels().first()
                val newHotels = response.filter { apiHotel ->
                    hotelsInDao.none { daoHotel -> daoHotel.id == apiHotel.id }
                }
                _isLoading.value = false

                withContext(Dispatchers.IO) {
                    for (hotel in newHotels) {
                        val existingHotel = hotelDao.getHotelById(hotel.id)
                        if (existingHotel == null) {
                            hotelDao.add(hotel)
                        } else {
                            hotelDao.update(hotel)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("Doing", e.toString())
            }
        }
    }

    fun sortByDistance() {
        val sortedHotels = _hotels.value?.sortedBy { it.distance } ?: emptyList()
        _hotels.value = sortedHotels
    }

    fun sortByRoom() {
        val hotelsList: List<Hotel> = _hotels.value?.toList() ?: emptyList()
        _hotels.value = hotelsList.sortedBy { hotel ->
            calculateAvailableRooms(hotel.suitesAvailability)
        }
    }

    private fun calculateAvailableRooms(suitesAvailability: String): Int {
        return suitesAvailability.split(":")
            .count { it.isNotEmpty() }
    }

}
