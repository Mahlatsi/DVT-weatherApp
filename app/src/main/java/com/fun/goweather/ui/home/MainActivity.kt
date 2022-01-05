package com.`fun`.goweather.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.goweather.R
import com.`fun`.goweather.helpers.utils.DataState
import com.`fun`.goweather.helpers.utils.NetworkHelper
import com.`fun`.goweather.helpers.utils.ResourceDataStatus
import com.`fun`.goweather.helpers.utils.utils.DataConverter
import com.`fun`.goweather.helpers.utils.utils.StringUtil
import com.`fun`.goweather.injection.ViewModelFactory
import com.`fun`.goweather.model.DescriptionType
import com.`fun`.goweather.model.Forecast
import com.`fun`.goweather.model.WeatherDt
import com.`fun`.goweather.model.favorite.FavoriteList
import com.`fun`.goweather.ui.BaseActivity
import com.`fun`.goweather.ui.favorite.FavoriteActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mainViewModel: MainViewModel

    override val contentResourceId: Int
        get() = R.layout.activity_main

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = this.viewModelFactory.let {
            ViewModelProvider(this, it).get(MainViewModel::class.java)
        }

        initCheck()

        forecast_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val forecast = ForecastAdapter()
        forecast_list.adapter = forecast

        val weatherObserver = Observer<ResourceDataStatus<WeatherDt?>> { weatherResource ->
            when (weatherResource.state) {
                DataState.LOADING -> {
                    progress_bar.show()
                }
                DataState.SUCCESS -> {
                    if (weatherResource != null) {
                        val description: String =
                            weatherResource.data?.weather?.get(0)?.description.toString()
                        val currentTemp: String =
                            StringUtil.validateTemp(weatherResource.data?.main?.temp)
                        val minTemp: String =
                            StringUtil.validateTemp(weatherResource.data?.main?.tempMin)
                        val maxTemp: String =
                            StringUtil.validateTemp(weatherResource.data?.main?.tempMax)
                        viewWeatherDetails(description, currentTemp, minTemp, maxTemp)
                        progress_bar.hide()
                    }
                }
                DataState.ERROR -> {
                    progress_bar.hide()
                    Toast.makeText(this, weatherResource.error, Toast.LENGTH_LONG).show()
                }
            }
        }

        val forecastObserver = Observer<ResourceDataStatus<Forecast?>> { forecastResource ->
            when (forecastResource.state) {
                DataState.LOADING -> {
                    forecast_progress_bar.show()
                }
                DataState.SUCCESS -> {
                    forecast.setData(forecastResource.data!!.daily)
                    forecast_progress_bar.hide()
                }
                DataState.ERROR -> {
                    forecast_progress_bar.show()
                    Toast.makeText(this, forecastResource.error, Toast.LENGTH_LONG).show()
                }
            }
        }

        val favoriteObserver =
            Observer<ResourceDataStatus<List<FavoriteList>>> { favoriteResource ->
                when (favoriteResource.state) {
                    DataState.LOADING -> {
                        progress_bar.show()
                        forecast_progress_bar.show()
                    }
                    DataState.SUCCESS -> {
                        val dataConverter = DataConverter()
                        if (favoriteResource.data!!.isNotEmpty()) {
                            val description: String =
                                favoriteResource.data!![0].description.toString()
                            val currentTemp: String =
                                StringUtil.validateTemp(favoriteResource.data!![0].temp?.toDouble())
                            val minTemp: String =
                                StringUtil.validateTemp(favoriteResource.data!![0].tempMin?.toDouble())
                            val maxTemp: String =
                                StringUtil.validateTemp(favoriteResource.data!![0].tempMax?.toDouble())
                            viewWeatherDetails(description, currentTemp, minTemp, maxTemp)
                            forecast.setData(dataConverter.toDailyList(favoriteResource.data!![0].forecast))
                        } else {
                            showErrorDialog(getString(R.string.error_connectivity))
                        }
                        progress_bar.hide()
                        forecast_progress_bar.hide()
                    }
                    DataState.ERROR -> {
                        progress_bar.hide()
                        forecast_progress_bar.hide()
                        Toast.makeText(this, favoriteResource.error, Toast.LENGTH_LONG).show()
                    }
                }
            }

        mainViewModel.getFavoriteLiveData().observe(this, favoriteObserver)
        mainViewModel.getWeatherLiveData().observe(this, weatherObserver)
        mainViewModel.getWeatherForecastLiveData().observe(this, forecastObserver)
    }

    @SuppressLint("ResourceAsColor")
    fun viewWeatherDetails(
        description: String,
        currentTemp: String,
        minTemp: String,
        maxTemp: String
    ) {
        view_group.visibility = View.VISIBLE
        temp.text = getString(R.string.celsius, currentTemp)
        conditions_title.text = description
        current_temp_value.text = getString(R.string.celsius, currentTemp)
        max_temp_value.text = getString(R.string.celsius, maxTemp)
        min_temp_value.text = getString(R.string.celsius, minTemp)

        when (description) {
            DescriptionType.CLEAR_SKIES -> {
                forcast_list_view.setBackgroundResource(R.color.sunny)
                description_view.setBackgroundResource(R.drawable.forest_sunny)
            }
            DescriptionType.RAINY -> {
                forcast_list_view.setBackgroundResource(R.color.rainy)
                description_view.setBackgroundResource(R.drawable.forest_rainy)
            }
            DescriptionType.WINDY -> {
                forcast_list_view.setBackgroundResource(R.color.cloudy)
                description_view.setBackgroundResource(R.drawable.forest_cloudy)
            }
            DescriptionType.OVERCAST_CLOUDS -> {
                forcast_list_view.setBackgroundResource(R.color.cloudy)
                description_view.setBackgroundResource(R.drawable.forest_cloudy)
            }
            DescriptionType.BROKEN_CLOUDS -> {
                forcast_list_view.setBackgroundResource(R.color.sunny)
                description_view.setBackgroundResource(R.drawable.forest_sunny)
            }
            DescriptionType.FEW_CLOUDS -> {
                forcast_list_view.setBackgroundResource(R.color.sunny)
                description_view.setBackgroundResource(R.drawable.forest_sunny)
            }
            DescriptionType.SNOW -> {
                forcast_list_view.setBackgroundResource(R.color.rainy)
                description_view.setBackgroundResource(R.drawable.forest_rainy)
            }
            DescriptionType.SCATTERED_CLOUDS -> {
                forcast_list_view.setBackgroundResource(R.color.cloudy)
                description_view.setBackgroundResource(R.drawable.forest_cloudy)
            }
            DescriptionType.THUNDER -> {
                forcast_list_view.setBackgroundResource(R.color.rainy)
            }
            DescriptionType.MIST -> {
                forcast_list_view.setBackgroundResource(R.color.cloudy)
                description_view.setBackgroundResource(R.drawable.forest_cloudy)
            }
        }
    }

    private fun initCheck() {
        if (NetworkHelper.isNetworkAvailable(this)) {
            mainViewModel.subscribeToCurrentWeather()
        } else {
            mainViewModel.getCachedFavoriteData()
        }
    }

    fun showPopUp(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.favorite_menu, popupMenu.menu)
        // Hide the download statement menu item

        if (!NetworkHelper.isNetworkAvailable(this)) {
            // Hide the add to favorite menu item
            val filterMenuItem: MenuItem = popupMenu.menu.findItem(R.id.add_to_favorite)
            filterMenuItem.isVisible = false
        }
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.view_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                }
                R.id.add_to_favorite -> {
                    mainViewModel.saveLocation()
                }
            }
            true
        }

    }
}
