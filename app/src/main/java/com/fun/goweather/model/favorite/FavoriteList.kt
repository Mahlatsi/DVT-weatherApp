package com.`fun`.goweather.model.favorite

import android.telecom.Call
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.`fun`.goweather.helpers.utils.utils.DataConverter
import com.`fun`.goweather.model.forecast.Daily


@Entity(tableName = "favoritelist")
class FavoriteList {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null

    @ColumnInfo(name = "temperature")
    var temp: String? = null

    @ColumnInfo(name = "temperatureMin")
    var tempMin: String? = null

    @ColumnInfo(name = "temperatureMax")
    var tempMax: String? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "date")
    var date: String? = null

    @ColumnInfo(name = "latitude")
    var latitude: String? = null

    @ColumnInfo(name = "longitude")
    var longitude: String? = null

    @ColumnInfo(name = "location")
    var location: String? = null

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name = "forecast")
    var forecast: String? = null
}