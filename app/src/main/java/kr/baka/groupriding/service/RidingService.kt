package kr.baka.groupriding.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.viewmodel.MainViewModel
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class RidingService: Service() {

    private val tag = this::class.simpleName

    private val timeInterval:Long = 1000
    private var threadStopFlag:Boolean = false
    private var tick = 0
    private var previousLocation:Location? = null
    private var viewModel:MainViewModel? = null
    private var distance = 0
    private var speed = 0
    private var sumOfSpeed:Long = 0
    private var speedSamplingCount:Long = 0
    private val myLocationListener = MyLocationListener()
    private lateinit var locationManager:LocationManager

    private var timerInterval:Long=1000
    private var timer:Timer = Timer()
    private var myTimerTask:MyTimerTask = MyTimerTask()
    private var ridingTime:Time = Time(0)
    private var restTime:Time = Time(0)
    private lateinit var mainVM:MainViewModel

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(tag,"onBind")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.v(tag,"onCreate")
        mainVM = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MainViewModel::class.java)
        initData()
        startForegroundNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.v(tag,"onStartCommand")

        timer.schedule(myTimerTask,0,timerInterval)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            onDestroy()
            return START_NOT_STICKY
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeInterval, 0f,myLocationListener)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        Log.v(tag,"onDestroy")
        timer.cancel()
        locationManager.removeUpdates(myLocationListener)
        super.onDestroy()
    }
    
    private fun startForegroundNotification(){
        var builder:NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel("lockChannel", "잠금화면 사용중 알림", NotificationManager.IMPORTANCE_HIGH)
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(mChannel)
                NotificationCompat.Builder(this,"lockChannel")
            } else {
                NotificationCompat.Builder(this)
            }

        builder.setSmallIcon(android.R.drawable.stat_notify_sync_noanim)
        builder.setContentText("서비스가 실행중입니다")
        startForeground(1, builder.build())
    }

    private fun initData(){
        mainVM.layoutTopLeft.value!!.setData("0")
        mainVM.layoutTopRight.value!!.setData("0")
        mainVM.layoutMiddle.value!!.setData("0")
        mainVM.layoutBottomLeft.value!!.setData("0")
        mainVM.layoutBottomRight.value!!.setData("0")

//        App.avgSpeedLiveData.value = "0"
//        App.distanceLiveData.value = "0"
//        App.speedLiveData.value = "0"
//        App.ridingTimeLiveData.value = "0:00:00"
//        App.restTimeLiveData.value = "0:00:00"
    }

    inner class MyLocationListener:LocationListener{
        override fun onLocationChanged(location: Location?) {
            if(location!=null){

//                speed.value = (location.speed*3600/1000).toInt()
//                if(previousLocation!=null && speed.value!!>0){
//                    val newDistance = previousLocation!!.distanceTo(location) // m/s
//                    distance += (newDistance*100).toInt() //cm
//                }

                if(previousLocation!=null){
                    val newDistance = previousLocation!!.distanceTo(location) // m/s
                    speed = (newDistance*3600/1000).toInt()
                    distance += (newDistance*100).toInt() //cm
                }

                if (speed>0) {
                    sumOfSpeed += speed
                    speedSamplingCount++
                }

                if (speedSamplingCount>0) App.avgSpeedLiveData.value = (sumOfSpeed/speedSamplingCount).toString()
                App.distanceLiveData.value = (distance/100).toString()
                App.speedLiveData.value = speed.toString()
                previousLocation = location
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            Log.v(tag,"onStatusChanged")
        }

        override fun onProviderEnabled(provider: String?) {
            Log.v(tag,"onProviderEnabled")
        }

        override fun onProviderDisabled(provider: String?) {
            Log.v(tag,"onProviderDisabled")
        }
    }

    inner class MyTimerTask : TimerTask() {
        override fun run() {
            if (speed>0){
                ridingTime.time = ridingTime.time+1000
                val format = SimpleDateFormat("H:mm:ss")
                format.timeZone = TimeZone.getTimeZone("UTC")
                App.ridingTimeLiveData.postValue(format.format(Date(ridingTime.time)))
            }
            else if(speed==0){
                restTime.time = restTime.time+1000
                val format = SimpleDateFormat("H:mm:ss")
                format.timeZone = TimeZone.getTimeZone("UTC")
                App.restTimeLiveData.postValue(format.format(Date(restTime.time)))
            }

        }
    }
}