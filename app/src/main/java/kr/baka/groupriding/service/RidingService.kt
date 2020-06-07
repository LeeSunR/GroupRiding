package kr.baka.groupriding.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlin.concurrent.thread

class RidingService: Service() {

    private val timeInterval:Long = 1000
    private var threadStopFlag:Boolean = false
    private var tick = 0
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.e("hey","onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("hey","onStartCommand")
        startThread()
        return START_STICKY
    }

    override fun onDestroy() {

        Log.e("hey","onDestroy")
        threadStopFlag=true
        super.onDestroy()
    }

    private fun startThread(){
        thread {
            threadStopFlag=false
            while (!threadStopFlag){
                Thread.sleep(timeInterval)
                Log.e("hey",tick++.toString())
            }
        }.run()
    }

}