package kr.baka.groupriding.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kr.baka.groupriding.R
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.repository.storage.MySharedPreferences
import java.net.Inet4Address
import java.net.NetworkInterface

object SettingRepository{
    private val context = App.context
    private val sharedPreferences =
        MySharedPreferences(App.context)

    var nickname: String?
        get() = sharedPreferences.nickname
        set(value) { sharedPreferences.nickname = value }

    var maxSpeed: Int
        get() = sharedPreferences.maxSpeed
        set(value) {sharedPreferences.maxSpeed = value}

    var samplingInterval: Int
        get() = sharedPreferences.samplingInterval
        set(value) {sharedPreferences.samplingInterval = value}

    val inviteCode = MutableLiveData<String>()

    fun getHostAddress():String{
        val address = getIPV4()
        val list = address!!.split(".")
        return if (list[0]=="192" && list[1]=="168" && list[2]=="35") context.getString(R.string.groupRidingServiceHostLAN)
        else context.getString(R.string.groupRidingServiceHostWAN)
    }

    private fun getIPV4():String?{
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()){
            val networkInterface = en.nextElement()
            val enumIpAddr = networkInterface.inetAddresses
            while(enumIpAddr.hasMoreElements()){
                val inetAddress = enumIpAddr.nextElement()
                //루프백이 아니고, IPv4가 맞다면 리턴~~~
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    Log.e("Address",inetAddress.getHostAddress().toString())
                    return inetAddress.getHostAddress().toString()
                }
            }
        }
        return null
    }
}