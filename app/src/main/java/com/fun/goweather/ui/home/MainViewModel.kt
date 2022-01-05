package com.`fun`.goweather.ui.home

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.`fun`.goweather.dao.FavoriteDatabase
import com.`fun`.goweather.helpers.utils.LocationTracker
import com.`fun`.goweather.helpers.utils.ResourceDataStatus
import com.`fun`.goweather.helpers.utils.utils.DataConverter
import com.`fun`.goweather.helpers.utils.utils.StringUtil
import com.`fun`.goweather.model.Forecast
import com.`fun`.goweather.model.WeatherDt
import com.`fun`.goweather.model.favorite.FavoriteList
import com.`fun`.goweather.model.forecast.Daily
import com.`fun`.goweather.webservice.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class MainViewModel @Inject constructor(
    application: Application,
    private val service: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val favoriteDatabase: FavoriteDatabase
) :
    AndroidViewModel(application) {

    private val weatherLiveData: MutableLiveData<ResourceDataStatus<WeatherDt?>> =
        MutableLiveData<ResourceDataStatus<WeatherDt?>>()

    private val weatherForecastLiveData: MutableLiveData<ResourceDataStatus<Forecast?>> =
        MutableLiveData<ResourceDataStatus<Forecast?>>()

    private val favoriteLiveData: MutableLiveData<ResourceDataStatus<List<FavoriteList>>> =
        MutableLiveData<ResourceDataStatus<List<FavoriteList>>>()

    private val compositeDisposable = CompositeDisposable()

    private var activeWeatherLocation: WeatherDt? = null

    private var forecastList: List<Daily?>? = ArrayList()

    fun getWeatherLiveData(): LiveData<ResourceDataStatus<WeatherDt?>> {
        return weatherLiveData
    }

    fun getWeatherForecastLiveData(): LiveData<ResourceDataStatus<Forecast?>> {
        return weatherForecastLiveData
    }

    fun getFavoriteLiveData(): LiveData<ResourceDataStatus<List<FavoriteList>>> {
        return favoriteLiveData
    }

    fun saveLocation() {
        val favorite = FavoriteList()
        val dataConverter = DataConverter()
        favorite.latitude = activeWeatherLocation?.coord?.lat.toString()
        favorite.longitude = activeWeatherLocation?.coord?.lon.toString()
        favorite.temp = StringUtil.validateTemp(activeWeatherLocation?.main?.temp)
        favorite.tempMin = StringUtil.validateTemp(activeWeatherLocation?.main?.tempMin)
        favorite.tempMax = StringUtil.validateTemp(activeWeatherLocation?.main?.tempMax)
        favorite.description = activeWeatherLocation?.weather?.get(0)?.description
        favorite.location = getLocationName(
            activeWeatherLocation?.coord?.lat!!,
            activeWeatherLocation?.coord?.lon!!
        )
        favorite.forecast = dataConverter.fromDailyList(forecastList)
        subscribeToDataPersist(favorite)
    }

    private fun subscribeToDataPersist(favorite: FavoriteList) {
        val disposable: Disposable? = favoriteDatabase.favoriteDao()?.addData(favorite)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({},
                { t: Throwable? -> })

        disposable?.let { compositeDisposable.add(it) }
    }

    fun getLocationName(lat: Double, lon: Double): String {
        var cityName = ""
        val geocoder = Geocoder(getApplication(), Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
            cityName = addresses[0].getAddressLine(0)
            Log.d("City..........", cityName)

        } catch (io: Exception) {
            io.stackTrace
        }
        return cityName
    }

    fun subscribeToCurrentWeather() {
        Log.d("Location track..........", locationTracker.getLatitude().toString())
        val disposable: Disposable = service.getUserCurrentWeather(
            locationTracker.getLatitude().toString(),
            locationTracker.getLongitude().toString()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { subscribeToCurrentForecast() }
            .doOnSubscribe { weatherLiveData.value = ResourceDataStatus.postLoading() }
            .subscribe(
                { response ->
                    activeWeatherLocation = response.body()
                    weatherLiveData.value = ResourceDataStatus.postSuccess(response.body())
                    Log.d("Success..........", response.body()?.main?.temp.toString())
                }, { throwable ->
                    weatherLiveData.value =
                        throwable.message?.let { ResourceDataStatus.postError(it) }
                    Log.d("Error..........", throwable.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    private fun subscribeToCurrentForecast() {
        val disposable: Disposable = service.getCurrentLocationWeatherForecast(
            locationTracker.getLatitude().toString(),
            locationTracker.getLongitude().toString(),
            "hourly"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { weatherForecastLiveData.value = ResourceDataStatus.postLoading() }
            .subscribe(
                { response ->
                    weatherForecastLiveData.value = ResourceDataStatus.postSuccess(response.body())
                    this.forecastList = response.body()?.daily
                    Log.d("Forecast success..........", response.body()!!.timezone)
                }, { throwable ->
                    weatherForecastLiveData.value =
                        throwable.message?.let { ResourceDataStatus.postError(it) }
                    Log.d("ForeCast error..........", throwable.message.toString())
                })
        compositeDisposable.add(disposable)
    }

    fun getCachedFavoriteData() {
        val disposable: Disposable? = favoriteDatabase.favoriteDao()?.getFavoriteData()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                favoriteLiveData.value = ResourceDataStatus.postSuccess(it)
            }, { t: Throwable? ->
                favoriteLiveData.value = ResourceDataStatus.postError(t?.message.toString())
                Log.d("Room Db Error..", t?.message.toString())
            })
        compositeDisposable.add(disposable!!)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        if (favoriteDatabase.isOpen) {
            favoriteDatabase.close()
        }
    }
}