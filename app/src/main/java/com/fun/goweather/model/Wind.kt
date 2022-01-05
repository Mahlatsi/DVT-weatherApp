package com.`fun`.goweather.model

import com.google.gson.annotations.SerializedName

class Wind {
    @SerializedName("speed")
    var speed: Double? = null

    @SerializedName("deg")
    var deg: Int? = null
}