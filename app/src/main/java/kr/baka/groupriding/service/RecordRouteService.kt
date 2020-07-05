package kr.baka.groupriding.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.naver.Map
import kr.baka.groupriding.repository.LocationLiveData
import kr.baka.groupriding.repository.RouteRepository
import kr.baka.groupriding.repository.ServiceStatusLiveData
import kr.baka.groupriding.repository.SettingRepository
import kr.baka.groupriding.repository.room.entity.RouteEntity
import kr.baka.groupriding.repository.room.entity.RouteSubEntity
import kr.baka.groupriding.view.dialog.GroupCodeShowDialog
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class RecordRouteService: Service() {

    private val TAG = this::class.simpleName
    private val locationObserver:LocationObserver = LocationObserver()

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(TAG,"onBind")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.v(TAG,"onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.v(TAG,"onStartCommand")
        ServiceStatusLiveData.recordingService.postValue(true)
        startForegroundNotification()
        LocationLiveData.observeForever(locationObserver)
        return START_STICKY
    }

    override fun onDestroy() {
        Log.v(TAG,"onDestroy")
        sendRecordFinish()
        LocationLiveData.removeObserver(locationObserver)
        Map.clear()
        ServiceStatusLiveData.recordingService.postValue(false)
        super.onDestroy()
    }

    private fun startForegroundNotification(){
        var builder:NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel("recordRouteChannel", "경로 녹화", NotificationManager.IMPORTANCE_HIGH)
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(mChannel)
                NotificationCompat.Builder(this,"recordRouteChannel")
            } else {
                NotificationCompat.Builder(this)
            }

        builder.setSmallIcon(android.R.drawable.stat_notify_sync_noanim)
        builder.setContentText("경로 녹화중입니다")
        startForeground(1, builder.build())
    }

    private fun sendRecordFinish(){
        val list = Map.pathArrayList
        val intent = Intent()
        intent.putExtra("list",list)
        intent.action = "recordFinishBroadcast"
        sendBroadcast(intent)
    }

    inner class LocationObserver: Observer<Location> {
        override fun onChanged(location: Location?) {
            if (location!!.provider==LocationManager.GPS_PROVIDER){
                Map.addPath(LatLng(location))
            }
        }
    }
}