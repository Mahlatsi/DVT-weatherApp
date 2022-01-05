package com.`fun`.goweather.helpers.utils.utils

import androidx.room.TypeConverter
import com.`fun`.goweather.model.forecast.Daily
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class DataConverter {

    @TypeConverter
    fun fromDailyList(daily: List<Daily?>?): String? {
        if (daily == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.toJson(daily, type)
    }

    @TypeConverter
    fun toDailyList(toDailyString: String?): List<Daily>? {
        if (toDailyString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson<List<Daily>>(toDailyString, type)
    }

}