package com.`fun`.goweather.dao

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FavoriteModule {

    @Provides
    @Singleton
    fun provideLocationDao(favoriteDatabase: FavoriteDatabase): FavoriteDao? {
        return favoriteDatabase.favoriteDao()
    }
}