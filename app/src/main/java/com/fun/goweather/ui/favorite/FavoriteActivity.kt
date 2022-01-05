package com.`fun`.goweather.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.`fun`.goweather.R
import com.`fun`.goweather.helpers.utils.DataState
import com.`fun`.goweather.helpers.utils.ResourceDataStatus
import com.`fun`.goweather.injection.ViewModelFactory
import com.`fun`.goweather.model.favorite.FavoriteList
import com.`fun`.goweather.ui.BaseActivity
import com.`fun`.goweather.ui.map.MapsActivity
import com.`fun`.goweather.ui.favorite.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FavoriteActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mainViewModel: FavoriteViewModel

    override val contentResourceId: Int
        get() = R.layout.activity_favorite

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = this.viewModelFactory.let {
            ViewModelProvider(this, it).get(FavoriteViewModel::class.java)
        }

        setUpActionBar(getString(R.string.favorites))

        favorite_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val favoriteAdapter = FavoriteAdapter()
        favorite_list.adapter = favoriteAdapter

        val favoriteObserver =
            Observer<ResourceDataStatus<List<FavoriteList>>> { favoriteResource ->
                when (favoriteResource.state) {
                    DataState.LOADING -> {
                        progress_bar.show()
                    }
                    DataState.SUCCESS -> {
                        val favList: List<FavoriteList> = favoriteResource.data!!
                        if (favList.isEmpty()) {
                            list_empty.visibility = View.VISIBLE
                            favorite_list.visibility = View.GONE
                        } else {
                            favorite_list.visibility = View.VISIBLE
                            list_empty.visibility = View.GONE
                        }

                        favoriteAdapter.setData(favoriteResource.data)
                    }
                    DataState.ERROR -> {
                        Toast.makeText(this, favoriteResource.error, Toast.LENGTH_LONG).show()
                    }
                }
            }

        mainViewModel.getFavoriteLiveData().observe(this, favoriteObserver)

        mainViewModel.getFavoriteData()

        favoriteAdapter.onItemClick = { favorite ->
            val favoriteListLocation: List<FavoriteList> = ArrayList()
            val favoriteList = FavoriteList()
            favoriteList.latitude = favorite.latitude
            favoriteList.longitude = favorite.longitude
            favoriteListLocation.toMutableList().add(favoriteList)
            openMapsActivity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_location_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        return if (itemId == R.id.view_all) {
            openMapsActivity()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun openMapsActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}
