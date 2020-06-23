package kr.baka.groupriding.etc

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData

class App:Application(){

    companion object{
        lateinit var context: Context
        lateinit var sharedPreferences : MySharedPreferences

        var speedLiveData = MutableLiveData<String>()
        var distanceLiveData = MutableLiveData<String>()
        var avgSpeedLiveData = MutableLiveData<String>()
        var maxSpeedLiveData = MutableLiveData<String>()
        var ridingTimeLiveData = MutableLiveData<String>()
        var restTimeLiveData = MutableLiveData<String>()
        var themeColor = MutableLiveData<Int>()
        var location = MutableLiveData<Location>()
        var sumOfSpeed = 0
        var countOfSampling = 0
        var isRidingServiceRunning = MutableLiveData<Boolean>()
        var isGroupRidingServiceRunning = MutableLiveData<Boolean>()
    }


    override fun onCreate() {
        context = applicationContext
        sharedPreferences = MySharedPreferences(context)
        themeColor.value = sharedPreferences.themeColor
        themeColor.observeForever {
            sharedPreferences.themeColor = it
        }
        isRidingServiceRunning.value = false
        isGroupRidingServiceRunning.value = false
        super.onCreate()
    }

}