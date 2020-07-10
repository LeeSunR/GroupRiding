package kr.baka.groupriding.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kr.baka.groupriding.R
import kr.baka.groupriding.model.Member
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.ViewModelFactory
import kr.baka.groupriding.lib.Map
import kr.baka.groupriding.repository.LocationLiveData
import kr.baka.groupriding.repository.ServiceStatusLiveData
import kr.baka.groupriding.repository.SettingRepository
import kr.baka.groupriding.viewmodel.MainViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class GroupRidingService: Service() {

    private val TAG = this::class.simpleName
    private val mSocket:Socket by lazy { IO.socket(SettingRepository.getHostAddress()) }
    private val locationObserver:LocationObserver = LocationObserver()
    private val disconnectedBroadcastReceiver = DisconnectedBroadcastReceiver()
    private val mainViewModel =  ViewModelProvider(App(), ViewModelFactory()).get(MainViewModel::class.java)

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
        startForegroundNotification()
        val requestCreateGroup = intent!!.getBooleanExtra("RequestCreateGroup",false)

        val filter = IntentFilter()
        filter.addAction("groupRidingDisconnected")
        registerReceiver(disconnectedBroadcastReceiver, filter)

        connect(requestCreateGroup)
        return START_STICKY
    }

    override fun onDestroy() {
        LocationLiveData.removeObserver(locationObserver)
        ServiceStatusLiveData.groupingService.postValue(false)
        unregisterReceiver(disconnectedBroadcastReceiver)
        Log.v(TAG,"onDestroy")
        groupFinishBroadcast()
        disconnect()
        Map.clear()
        super.onDestroy()
    }

    private fun connect(requestCreateGroup:Boolean){
        mSocket.on(Socket.EVENT_CONNECT, ConnectListener())
        mSocket.on(Socket.EVENT_DISCONNECT, DisconnectListener())
        mSocket.on(Socket.EVENT_CONNECT_ERROR, ConnectErrorListener())
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, ConnectErrorListener())
        mSocket.on("joinError", JoinErrorListener())
        mSocket.on("groupCreateCompleted", GroupCreateCompletedListener())
        mSocket.on("GroupMemberLocationReceived", GroupMemberLocationReceivedListener())
        mSocket.connect()
        if (requestCreateGroup) mSocket.emit("create","")
        else mSocket.emit("join",SettingRepository.inviteCode.value)
        LocationLiveData.observeForever(locationObserver)
    }

    private fun disconnect(){
        if (mSocket.connected()) mSocket.disconnect()
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
        builder.setContentText(getString(R.string.serviceMessageGroupRidingStart))
        startForeground(1, builder.build())
    }

    private fun groupFinishBroadcast(){
        val list = Map.getPath()
        val intent = Intent()
        intent.putExtra("list",list)
        intent.action = "groupFinishBroadcast"
        sendBroadcast(intent)
    }

    //
    // socket event
    //

    inner class ConnectListener : Emitter.Listener{
        override fun call(vararg args: Any?) {
            ServiceStatusLiveData.groupingService.postValue(true)
            Log.v(TAG,"connected")
        }
    }

    inner class DisconnectListener : Emitter.Listener{
        override fun call(vararg args: Any?) {
            Log.v(TAG,"disconnected")

            val intent = Intent()
            intent.action = "groupRidingDisconnected"
            sendBroadcast(intent)

        }
    }

    inner class ConnectErrorListener : Emitter.Listener{
        override fun call(vararg args: Any?) {
            Log.v(TAG,"connect error : "+args[0].toString())
        }
    }

    inner class JoinErrorListener : Emitter.Listener{
        override fun call(vararg args: Any?) {
            Log.e(TAG,"Error : "+ args[0].toString())

            val intent = Intent()
            intent.action = "joinErrorBroadcast"
            intent.putExtra("message",args[0].toString())
            sendBroadcast(intent)

            disconnect()
            //onDestroy()
        }
    }

    inner class GroupCreateCompletedListener : Emitter.Listener{
        override fun call(vararg args: Any?) {
            Log.e(TAG,"group created "+ args[0].toString())
            SettingRepository.inviteCode.postValue(args[0].toString())

            val intent = Intent()
            intent.action = "GroupCreateCompletedBroadcast"
            sendBroadcast(intent)
        }
    }

    inner class GroupMemberLocationReceivedListener : Emitter.Listener{
        override fun call(vararg args: Any?) {
            val jsonArray = JSONArray(args[0].toString())
            val memberArrayList = ArrayList<Member>()
            for (i in 0 until jsonArray.length()){
                val member = Member()
                member.nickname = jsonArray.getJSONObject(i).getString("nickname")
                member.latitude = jsonArray.getJSONObject(i).getDouble("latitude")
                member.longitude = jsonArray.getJSONObject(i).getDouble("longitude")
                member.lastDate = Date(jsonArray.getJSONObject(i).getLong("timeStamp"))
                member.owner = jsonArray.getJSONObject(i).getBoolean("owner")
                member.me = (jsonArray.getJSONObject(i).getString("id") == mSocket.id())
                member.id = jsonArray.getJSONObject(i).getString("id")
                memberArrayList.add(member)
            }
            mainViewModel.members.postValue(memberArrayList)
        }


    }

    //
    // observer event
    //

    inner class LocationObserver: Observer<Location> {
        override fun onChanged(location: Location?) {
            if(location!=null && location.provider == LocationManager.GPS_PROVIDER){
                val json = JSONObject()
                json.put("latitude",location.latitude)
                json.put("longitude",location.longitude)
                mSocket.emit("update",json)
            }
        }
    }

    inner class DisconnectedBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onDestroy()
        }
    }
}