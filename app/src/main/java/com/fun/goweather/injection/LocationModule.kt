package com.`fun`.goweather.injection

import android.app.Application
import android.content.Context
import com.`fun`.goweather.helpers.utils.LocationTracker
import dagger.Module
import dagger.Provides

@Module
class LocationModule {
    @Provides
    fun providesUserService(context: Application): LocationTracker {
        return LocationTracker(context)
    }
}