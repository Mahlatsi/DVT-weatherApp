package com.`fun`.goweather.model

import com.google.gson.annotations.SerializedName

class Main {
    @SerializedName("temp")
    var temp: Double? = null

    @SerializedName("feelsLike")
    var feelsLike: Double? = null

    @SerializedName("temp_min")
    var tempMin: Double? = null

    @SerializedName("temp_max")
    var tempMax: Double? = null

    @SerializedName("pressure")
    var pressure: Int? = null

    @SerializedName("humidity")
    var humidity: Int? = null
}