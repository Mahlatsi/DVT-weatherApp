package com.`fun`.goweather.model


@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class DescriptionType {
    companion object {
        var CLEAR_SKIES = "clear sky"
        var OVERCAST_CLOUDS = "overcast clouds"
        var RAINY = "rainy"
        var WINDY = "windy"
        var BROKEN_CLOUDS = "broken clouds"
        var SCATTERED_CLOUDS = "scattered clouds"
        var FEW_CLOUDS = "few clouds"
        var THUNDER = "thunderstorm"
        var SNOW = "snow"
        var MIST = "mist"
    }
}