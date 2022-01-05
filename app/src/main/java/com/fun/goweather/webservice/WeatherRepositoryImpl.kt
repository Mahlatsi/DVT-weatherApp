package com.`fun`.goweather.webservice

import com.`fun`.goweather.model.Forecast
import com.`fun`.goweather.model.WeatherDt
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(retrofit: Retrofit) :
    WeatherRepository {

    companion object {
        private const val UNITS =
            "metric"
        private const val KEY =
            "f078ee1dc69f4030000271c30ed8f594"
    }

    private val api = retrofit.create(Api::class.java)

    override fun getUserCurrentWeather(
        latitude: String,
        longitude: String
    ): Observable<Response<WeatherDt>> {
        return api.getUserCurrentWeather(latitude, longitude, KEY, UNITS)
    }

    override fun getCurrentLocationWeatherForecast(
        latitude: String,
        longitude: String,
        cnt: String
    ): Observable<Response<Forecast>> {
        return api.getCurrentLocationWeatherForecast(latitude, longitude, cnt, KEY, UNITS)
    }
}