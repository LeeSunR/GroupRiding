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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.ViewModelFactory
import kr.baka.groupriding.viewmodel.MainViewModel

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
            Toast.makeText(App.context,"onStatusChanged : $provider\nstatus : $status",Toast.LENGTH_SHORT).show()
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
        Log.e("onActive","onActive")
        if (ActivityCompat.checkSelfPermission(App.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.2f, listener)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0.2f, listener)
        }
        super.onActive()
    }
}