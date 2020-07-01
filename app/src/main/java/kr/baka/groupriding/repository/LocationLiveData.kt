package kr.baka.groupriding.repository

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import kr.baka.groupriding.etc.App

object LocationLiveData : LiveData<Location>(){
    private val locationManager by lazy { App.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val listener = object:LocationListener{
        override fun onLocationChanged(location: Location?) {
            if(location!=null){
                Log.e("onLocationChanged",location.provider)
                value = location
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            Log.e("onStatusChanged","provider:$provider")
        }

        override fun onProviderEnabled(provider: String?) {
            Log.e("onProviderEnabled","onProviderEnabled : $provider")
        }

        override fun onProviderDisabled(provider: String?) {
            Log.e("onProviderDisabled","onProviderDisabled : $provider")
        }
    }

    override fun onInactive() {
        locationManager.removeUpdates(listener)
        super.onInactive()
    }

    public override fun onActive() {
        if (ActivityCompat.checkSelfPermission(App.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0f, listener)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0f, listener)
        }
        super.onActive()
    }
}