package com.`fun`.goweather.ui.map

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.`fun`.goweather.dao.FavoriteDatabase
import com.`fun`.goweather.helpers.utils.ResourceDataStatus
import com.`fun`.goweather.model.favorite.FavoriteList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapsViewModel @Inject constructor(
    application: Application,
    private val favoriteDatabase: FavoriteDatabase
) :
    AndroidViewModel(application) {

    private var favoriteListLocation: List<FavoriteList> = ArrayList()
    private val favoriteLiveData: MutableLiveData<ResourceDataStatus<List<FavoriteList>>> =
        MutableLiveData<ResourceDataStatus<List<FavoriteList>>>()


    private val compositeDisposable = CompositeDisposable()

    fun getFavoriteLiveData(): LiveData<ResourceDataStatus<List<FavoriteList>>> {
        return favoriteLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        if (favoriteDatabase.isOpen) {
            favoriteDatabase.close()
        }
    }

    fun getFavoriteData() {
        val disposable: Disposable? = favoriteDatabase.favoriteDao()?.getFavoriteData()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                this.favoriteListLocation = it
                favoriteLiveData.value = ResourceDataStatus.postSuccess(it)
            }, { t: Throwable? ->
                favoriteLiveData.value = ResourceDataStatus.postError(t?.message.toString())
                Log.d("Room Db Error..", t?.message.toString())
            })
        compositeDisposable.add(disposable!!)
    }
}