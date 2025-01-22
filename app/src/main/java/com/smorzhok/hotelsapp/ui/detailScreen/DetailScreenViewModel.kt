package com.smorzhok.hotelsapp.ui.detailScreen

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smorzhok.hotelsapp.local.HotelsDatabase
import com.smorzhok.hotelsapp.model.HotelDetail
import com.smorzhok.hotelsapp.remote.HotelApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val appDatabase = HotelsDatabase.getInstance(application)
    private val hotelsDetailDao = HotelsDatabase.getInstance(application).hotelDetailDao()
    private val _hotel = MutableLiveData<HotelDetail>()
    val hotel: LiveData<HotelDetail> get() = _hotel
    private val _image = MutableLiveData<Painter?>()
    val image: LiveData<Painter?> get() = _image


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    fun loadDetails(id: Int) {

        viewModelScope.launch {
            _isLoading.value = true
            val response = HotelApi.retrofitService.loadHotelsDetail(id)
            _hotel.value = response
            withContext(Dispatchers.IO) {
                val existingHotel = hotelsDetailDao.getHotelById(response.id)
                if (existingHotel == null) {
                    hotelsDetailDao.add(response)
                } else {
                    hotelsDetailDao.update(response)
                }
            }
            if (response.image != null)
                fetchImage(response.image)
            else _image.value = null
            _isLoading.value = false
        }
    }

    private suspend fun fetchImage(image: String) {
        val imageUrl = "https://github.com/iMofas/ios-android-test/raw/master/$image"
        val responseBody = HotelApi.retrofitService.loadImage(imageUrl)
        if (responseBody.isSuccessful) {
            val inputStream = responseBody.body()?.byteStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val croppedBitmap = Bitmap.createBitmap(
                bitmap,
                1,
                1,
                bitmap.asImageBitmap().width - 2,
                bitmap.asImageBitmap().height - 2
            )
            _image.value = BitmapPainter(croppedBitmap.asImageBitmap())
        } else _image.value = null
    }
}