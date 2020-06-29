package hu.bitraptors.fazakas.homework.location

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*


class LocationProviderForFoursquare(
    context: Context,
    private val locationUpdate: LocationUpdate
) {
    private var fusedLocationClient: FusedLocationProviderClient
            = LocationServices.getFusedLocationProviderClient(context)
    private val locationCallback = MyLocationCallback()

    companion object{
        private const val REQUEST_INTERVAL = 1000L
        private const val REQUEST_FASTEST_INTERVAL = 500L
    }

    interface LocationUpdate {
        fun onLocationUpdate(location: Location)
    }

    internal inner class MyLocationCallback : LocationCallback() {
       override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationUpdate.onLocationUpdate(locationResult.lastLocation)
        }
    }

    fun startLocationMonitoring() {
        val locationRequest = LocationRequest()
        locationRequest.interval = REQUEST_INTERVAL
        locationRequest.fastestInterval = REQUEST_FASTEST_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    fun stopLocationMonitoring() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


}
