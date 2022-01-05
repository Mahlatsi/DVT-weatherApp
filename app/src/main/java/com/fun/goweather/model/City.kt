package com.`fun`.goweather.model

import com.google.gson.annotations.SerializedName

class City {

    @SerializedName("coord")
    var coord: Coord? = null

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("population")
    var population: Double? = null

    @SerializedName("timezone")
    var timezone: String? = null

    @SerializedName("sunrise")
    var sunrise: String? = null

    @SerializedName("sunset")
    var sunset: String? = null
}