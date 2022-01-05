package com.`fun`.goweather.injection

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideContext(application: Application?): Context? {
        return application
    }

}