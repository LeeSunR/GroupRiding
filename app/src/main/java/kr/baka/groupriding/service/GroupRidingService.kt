package kr.baka.groupriding.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kr.baka.groupriding.model.Member
import kr.baka.groupriding.etc.App
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class GroupRidingService: Service() {

    private val TAG = this::class.simpleName
    private lateinit var mSocket:Socket

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(TAG,"onBind")
        return null
    }

    @SuppressLint("InvalidWakeLockTag")
    override fun onCreate() {
        super.onCreate()
        Log.v(TAG,"onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.v(TAG,"onStartCommand")
        //startForegroundNotification()
        val requestCreateGroup = intent!!.getBooleanExtra("RequestCreateGroup",false)
        connect(requestCreateGroup)
        return START_STICKY
    }

    override fun onDestroy() {
        disconnect()
        Log.v(TAG,"onDestroy")
        super.onDestroy()
    }

    private fun connect(requestCreateGroup:Boolean){
        mSocket = IO.socket("http://192.168.35.235:6060")
        mSocket.on(Socket.EVENT_CONNECT, ConnectListener())
        mSocket.on(Socket.EVENT_DISCONNECT, DisconnectListener())
        mSocket.on(Socket.EVENT_CONNECT_ERROR, ConnectErrorListener())
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, ConnectErrorListener())
        mSocket.on("joinError", JoinErrorListener())
        mSocket.on("groupCreateCompleted", GroupCreateCompletedListener())
        mSocket.on("GroupMemberLocationReceived", GroupMemberLocationReceivedListener())
        mSocket.connect()

        App.location.observeForever {
            val json = JSONObject()
            json.put("latitude",it.latitude)
            json.put("longitude",it.longitude)
            mSocket.emit("update",json)
        }

        if (requestCreateGroup) mSocket.emit("create","")
        else mSocket.emit("join",App.inviteCode.value)
    }

    private fun disconnect(){
        App.isGroupRidingServiceRunning.postValue(false)
        mSocket.disconnect()
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

    //
    // event
    //

    inner class ConnectListener : Emitter.Listener{
        override fun call(vararg args: Any?) {
            App.isGroupRidingServiceRunning.postValue(true)
            Log.v(TAG,"connected")
        }
    }

    inner class DisconnectListener : Emitter.Listener{
        override fun call(vararg args: Any?) {
            Log.v(TAG,"disconnected")
            onDestroy()
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

            onDestroy()
        }
    }

    inner class GroupCreateCompletedListener : Emitter.Listener{
        override fun call(vararg args: Any?) {
            Log.e(TAG,"group created "+ args[0].toString())
            App.inviteCode.postValue(args[0].toString())

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
            App.members.postValue(memberArrayList)
        }
    }
}