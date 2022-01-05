package com.`fun`.goweather.webservice

import com.`fun`.goweather.model.Forecast
import com.`fun`.goweather.model.WeatherDt
import io.reactivex.Observable
import retrofit2.Response

interface WeatherRepository {

    fun getUserCurrentWeather(
        latitude: String,
        longitude: String
    ): Observable<Response<WeatherDt>>

    fun getCurrentLocationWeatherForecast(
        latitude: String,
        longitude: String,
        cnt: String
    ): Observable<Response<Forecast>>

}
//-33.82106203
//33.82106203
//18.71752695
