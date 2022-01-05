package com.`fun`.goweather.injection

import android.app.Application
import com.`fun`.goweather.AppApplication
import com.`fun`.goweather.dao.FavoriteModule
import com.`fun`.goweather.webservice.WeatherRepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class, ActivityBuilder::class,
        AppModule::class, ViewModelBuilderModule::class, WeatherRepositoryModule::class,
        FavoriteModule::class, DataBaseModule::class, LocationModule::class]
)
interface AppComponent : AndroidInjector<AppApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    override fun inject(application: AppApplication)
}