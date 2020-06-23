package kr.baka.groupriding.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kr.baka.groupriding.etc.App

class GroupRidingService: Service() {

    private val tag = this::class.simpleName

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(tag,"onBind")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        App.isGroupRidingServiceRunning.value = true
        Log.v(tag,"onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.v(tag,"onStartCommand")
        return START_STICKY
    }

    override fun onDestroy() {
        App.isGroupRidingServiceRunning.value = false
        super.onDestroy()
    }
    
    private fun startForegroundNotification(){
        var builder:NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel("groupRidingChannel", "그룹라이딩", NotificationManager.IMPORTANCE_HIGH)
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(mChannel)
                NotificationCompat.Builder(this,"groupRidingChannel")
            } else {
                NotificationCompat.Builder(this)
            }

        builder.setSmallIcon(android.R.drawable.stat_notify_sync_noanim)
        builder.setContentText("그룹라이딩 서비스가 실행중입니다")
        startForeground(1, builder.build())
    }
}