package com.smorzhok.hotelsapp.ui.detailScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.smorzhok.hotelsapp.R
import com.smorzhok.hotelsapp.model.Hotel
import com.smorzhok.hotelsapp.model.HotelDetail

class DetailScreenActivity : AppCompatActivity() {

    private lateinit var composeView: ComposeView
    private lateinit var viewModel: DetailScreenViewModel

    @Composable
    fun DetailScreen(hotelDetail: HotelDetail, image: Painter?) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (image != null) {
                Image(
                    painter = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()

                )
            }

            Text(
                "Название: ${hotelDetail.name}", fontSize = 24.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                "Адрес: ${hotelDetail.address}", fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                "Рейтинг: ${hotelDetail.stars}/5.0", fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                "Свободные номера: ${hotelDetail.suitesAvailability}", fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                "Расстояние от центра города: ${hotelDetail.distance}", fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                "Координаты: ${hotelDetail.lat};${hotelDetail.lon}", fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    @Composable
    fun LoadingScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator(color = Color.Black)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        viewModel = ViewModelProvider(this, DetailScreenViewModelFactory(application))
            .get(DetailScreenViewModel::class.java)

        composeView = findViewById(R.id.composeView)
        val hotel: Hotel? = intent.getParcelableExtra(EXTRA_HOTEL)
        if (hotel != null) {
            viewModel.loadDetails(hotel.id)

            composeView.setContent {
                val isLoading by viewModel.isLoading.observeAsState(initial = true)
                val hotelDetail by viewModel.hotel.observeAsState(initial = null)
                val image by viewModel.image.observeAsState(initial = null)

                if (isLoading) {
                    LoadingScreen()
                } else if (hotelDetail != null) {
                    DetailScreen(hotelDetail = hotelDetail!!, image)
                }
            }
        } else {
            Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    companion object {
        private const val EXTRA_HOTEL = "Hotel ID"
        fun newIntent(context: Context?, hotel: Hotel): Intent {
            val intent = Intent(context, DetailScreenActivity::class.java)
            intent.putExtra(EXTRA_HOTEL, hotel)
            return intent
        }
    }
}