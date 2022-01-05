package com.`fun`.goweather.model

import com.`fun`.goweather.model.forecast.Current
import com.`fun`.goweather.model.forecast.Daily
import com.google.gson.annotations.SerializedName

class Forecast {

    @SerializedName("lat")
    var lat: Double? = null

    @SerializedName("lon")
    var lon: Double? = null

    @SerializedName("timezone")
    var timezone: String? = null

    @SerializedName("timezone_offset")
    var timezone_offset: String? = null

    @SerializedName("current")
    var current: Current? = null

    @SerializedName("daily")
    var daily: List<Daily>? = null

}