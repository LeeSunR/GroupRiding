package kr.baka.groupriding.service

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
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.model.Information
import kr.baka.groupriding.viewmodel.MainViewModel
import kotlin.concurrent.thread

class RidingService: Service() {

    private val tag = this::class.simpleName

    private val timeInterval:Long = 100
    private var threadStopFlag:Boolean = false
    private var tick = 0
    private var previousLocation:Location? = null
    private var viewModel:MainViewModel? = null

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(tag,"onBind")
        return null
    }

    override fun onCreate() {
        Log.v(tag,"onCreate")
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(tag,"onStartCommand")
        startThread()
        return START_STICKY
    }

    override fun onDestroy() {
        Log.v(tag,"onDestroy")
        super.onDestroy()
    }

    private fun startThread(){
        val locationRequest = LocationRequest.create()
        val fusedLocationProviderClient = FusedLocationProviderClient(applicationContext)
        locationRequest.interval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            MyLocationCallback(),
            Looper.myLooper()
        )

    }


    inner class MyLocationCallback: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            val location = locationResult?.lastLocation

            if(previousLocation!=null){
                var floatArray:FloatArray = FloatArray(2)
                Location.distanceBetween(previousLocation!!.latitude,previousLocation!!.longitude,location!!.latitude,location!!.longitude,floatArray)

                var mfors = floatArray[0] * 3600/1000
                App.speedLiveData.value = mfors.toInt().toString()
            }
            //Log.e("LOCATION_CALLBACK",previousLocation?.speed.toString())

            previousLocation = location
            super.onLocationResult(locationResult)
        }
    }
}