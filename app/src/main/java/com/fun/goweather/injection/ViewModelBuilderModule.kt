package com.`fun`.goweather.injection

import androidx.lifecycle.ViewModel
import com.`fun`.goweather.ui.home.MainViewModel
import com.`fun`.goweather.ui.favorite.FavoriteViewModel
import com.`fun`.goweather.ui.map.MapsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@SuppressWarnings("ALL")
@Module
abstract class ViewModelBuilderModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindFavoriteViewModel(viewModel: FavoriteViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(MapsViewModel::class)
    abstract fun bindMapsViewModel(viewModel: MapsViewModel?): ViewModel?

}