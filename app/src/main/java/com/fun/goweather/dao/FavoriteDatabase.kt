package com.`fun`.goweather.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.`fun`.goweather.model.favorite.FavoriteList

@Database(entities = [FavoriteList::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao?

}