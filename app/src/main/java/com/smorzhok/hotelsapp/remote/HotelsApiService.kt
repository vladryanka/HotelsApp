package com.smorzhok.hotelsapp.remote

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.http.GET
import java.util.concurrent.TimeUnit
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.smorzhok.hotelsapp.model.Hotel
import com.smorzhok.hotelsapp.model.HotelDetail
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Url


private const val BASE_URL: String = "https://raw.githubusercontent.com/"

val json = Json {
    ignoreUnknownKeys = true
}

private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()

interface HotelsApiService {
    @GET("iMofas/ios-android-test/master/0777.json")
    suspend fun loadHotels(): List<Hotel>

    @GET("/iMofas/ios-android-test/master/{hotelId}.json")
    suspend fun loadHotelsDetail(@Path("hotelId") hotelId: Int): HotelDetail

    @GET
    suspend fun loadImage(@Url imageUrl: String): Response<ResponseBody>
}

object HotelApi {
    val retrofitService: HotelsApiService by lazy {
        retrofit.create(HotelsApiService::class.java)
    }
}

