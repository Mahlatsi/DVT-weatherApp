package com.`fun`.goweather.webservice

import com.`fun`.goweather.model.Forecast
import com.`fun`.goweather.model.WeatherDt
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    fun getUserCurrentWeather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Observable<Response<WeatherDt>>

    @GET("onecall")
    fun getCurrentLocationWeatherForecast(
        @Query("lat") lat: String?,
        @Query("lon") lon: String,
        @Query("exclude") cnt: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Observable<Response<Forecast>>
}