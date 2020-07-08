package kr.baka.groupriding.etc

import android.app.Application
import android.content.Context
import android.database.Observable
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kr.baka.groupriding.R
import kr.baka.groupriding.model.Member
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import kotlin.collections.ArrayList

class App:Application(), ViewModelStoreOwner {

    companion object{
        lateinit var context: Context
        //members
    }


    override fun onCreate() {
        context = applicationContext
        super.onCreate()
    }
    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }
    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }

}