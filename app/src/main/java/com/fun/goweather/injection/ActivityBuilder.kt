package com.`fun`.goweather.injection

import com.`fun`.goweather.ui.home.MainActivity
import com.`fun`.goweather.ui.map.MapsActivity
import com.`fun`.goweather.ui.favorite.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindFavorite(): FavoriteActivity

    @ContributesAndroidInjector
    abstract fun bindMapsActivity(): MapsActivity

}