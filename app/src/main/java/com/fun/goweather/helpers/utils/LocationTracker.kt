package com.`fun`.goweather.helpers.utils

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import javax.inject.Inject

class LocationTracker @Inject constructor(private val context: Context) : Service(),
    LocationListener {
    //---Gets the lass name------
    private val TAG = LocationTracker::class.java.name
    var isGPSEnabled = false
    var isNetworkEnabled = false
    var location: Location? = null
    private var latitude = 0.0
    private var longitude = 0.0

    //---------Declaring a location manager----
    private var locationManager: LocationManager? = null

    //-----Store location manager-----
    private var provider_info: String? = null

    //------ Try to get location is gps is enabled------
    val locaton: Unit
        get() {
            try {
                locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                //----Getting GPS status-----
                if (locationManager != null) {
                    isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                }

                //---Getting network status-----
                if (locationManager != null) {
                    isNetworkEnabled =
                        locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                }


                //------ Try to get location is gps is enabled------
                if (isGPSEnabled) {
                    isGPSEnabled = true
                    Log.d(TAG, "Using GPS services")
                    provider_info = LocationManager.GPS_PROVIDER

                    //----try to get location using network-------
                } else if (isNetworkEnabled) {
                    isNetworkEnabled = true
                    Log.d(TAG, "Using network services")
                    provider_info = LocationManager.NETWORK_PROVIDER
                }
                if (provider_info!!.isNotEmpty()) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    locationManager!!.requestLocationUpdates(
                        provider_info,
                        DISTANCE,
                        TIME1.toFloat(),
                        this
                    )
                    if (locationManager != null) {
                        location = locationManager!!.getLastKnownLocation(provider_info)
                        updateCoordinates()
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "ant connect to manager")
            }
        }

    //-----Tries to update latitude and longitude-------
    fun updateCoordinates() {
        if (location != null) {
            latitude = location!!.latitude
            longitude = location!!.longitude
        }
    }

    //-----Tries to get the longitude-------
    fun getLongitude(): Double {
        if (location != null) {
            longitude = location!!.longitude
        }
        return longitude
    }

    //-----Tries to get the latitude--------
    fun getLatitude(): Double {
        if (location != null) {
            latitude = location!!.latitude
        }
        return latitude
    }

    override fun onLocationChanged(location: Location) {}
    override fun onStatusChanged(
        provider: String,
        status: Int,
        extras: Bundle
    ) {
    }

    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        //------
        private const val DISTANCE: Long = 10
        private const val TIME1 = 1000 * 60.toLong()
    }

    init {
        locaton
    }
}