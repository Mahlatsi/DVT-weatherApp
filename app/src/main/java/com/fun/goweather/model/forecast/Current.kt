package com.`fun`.goweather.model.forecast

import com.`fun`.goweather.model.Weather
import com.google.gson.annotations.SerializedName

class Current {

    @SerializedName("sunrise")
    var sunrise: String? = null

    @SerializedName("sunset")
    var sunset: String? = null

    @SerializedName("pop")
    var pop: Int? = null

    @SerializedName("visibility")
    var visibility: Int? = null

    @SerializedName("pressure")
    var pressure: Int? = null

    @SerializedName("humidity")
    var humidity: Int? = null

    @SerializedName("dt")
    var dt: Long? = null

    @SerializedName("dt_txt")
    var dt_txt: String? = null

    @SerializedName("dew")
    var dew: String? = null

    @SerializedName("timezone")
    var timezone: Int? = null

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("wind_speed")
    var wind_speed: Double? = null

    @SerializedName("wind_deg")
    var wind_deg: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("cod")
    var cod: Int? = null

    @SerializedName("temp")
    var temp: Double? = null

    @SerializedName("weather")
    var weather: List<Weather>? = null
}