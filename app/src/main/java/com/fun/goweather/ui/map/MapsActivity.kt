package com.`fun`.goweather.ui.map

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.`fun`.goweather.R
import com.`fun`.goweather.helpers.utils.DataState
import com.`fun`.goweather.helpers.utils.ResourceDataStatus
import com.`fun`.goweather.injection.ViewModelFactory
import com.`fun`.goweather.model.favorite.FavoriteList
import com.`fun`.goweather.ui.BaseActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class MapsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mMap: GoogleMap
    private var favoriteListLocation: List<FavoriteList> = ArrayList()
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var placesClient: PlacesClient
    private lateinit var placeId: String

    override val contentResourceId: Int
        get() = R.layout.activity_maps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapsViewModel = this.viewModelFactory.let {
            ViewModelProvider(this, it).get(MapsViewModel::class.java)
        }

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.api_key), Locale.US)
        }

        placesClient = Places.createClient(this)
        setUpActionBar(getString(R.string.places))


        val favoriteObserver =
            Observer<ResourceDataStatus<List<FavoriteList>>> { favoriteResource ->
                when (favoriteResource.state) {
                    DataState.LOADING -> {
                        progress_bar.show()
                    }
                    DataState.SUCCESS -> {
                        mapFragment.getMapAsync {
                            setupMap(it)
                        }
                        this.favoriteListLocation = favoriteResource.data!!
                    }
                    DataState.ERROR -> {
                        Toast.makeText(this, favoriteResource.error, Toast.LENGTH_LONG).show()
                    }
                }
            }

        mapsViewModel.getFavoriteLiveData().observe(this, favoriteObserver)

        mapsViewModel.getFavoriteData()

    }

    fun getSelectedLocation(title: String) {

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment


        autocompleteFragment.setText(title)
        autocompleteFragment.setHint("Tap on any location...")
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                placeId = p0.id.toString()
                Log.d("Place id....", placeId)
                getPlaceDetails()
            }

            override fun onError(p0: Status) {
                Log.d("eRROR....", p0.statusMessage)

            }

        })
    }

    private fun setupMap(googleMap: GoogleMap?) {
        mMap = googleMap!!

        for (item in favoriteListLocation) {
            val latLng = LatLng(
                item.latitude!!.toDouble(), item.longitude!!.toDouble()
            )
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(item.location)

            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12F))
        }


        mMap.setOnMarkerClickListener(OnMarkerClickListener {
            // TODO Auto-generated method stub

            getSelectedLocation(it.title)
            return@OnMarkerClickListener true

            false
        })

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
    }

    fun getPlaceDetails() {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response
                Log.i("MapsActivity", "Place found: ${place.place.name}")
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e(TAG, "Place not found: ${exception.message}")
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }
}
