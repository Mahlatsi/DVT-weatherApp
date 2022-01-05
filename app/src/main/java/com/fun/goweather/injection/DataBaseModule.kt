package com.`fun`.goweather.injection

import android.app.Application
import androidx.room.Room
import com.`fun`.goweather.dao.FavoriteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun provideDao(context: Application): FavoriteDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteDatabase::class.java,
            "favorite"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}