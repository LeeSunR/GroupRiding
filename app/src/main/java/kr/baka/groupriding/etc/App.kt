package kr.baka.groupriding.etc

import android.app.Application
import android.content.Context
import android.database.Observable
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kr.baka.groupriding.R
import kr.baka.groupriding.model.Member
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import kotlin.collections.ArrayList

class App:Application(){

    companion object{
        lateinit var context: Context
        lateinit var sharedPreferences : MySharedPreferences

        //service flag

        //service flag
        var inviteCode = MutableLiveData<String>()

        //members
        var members = MutableLiveData<ArrayList<Member>>()

    }


    override fun onCreate() {
        context = applicationContext
        sharedPreferences = MySharedPreferences(context)
        super.onCreate()
    }

}