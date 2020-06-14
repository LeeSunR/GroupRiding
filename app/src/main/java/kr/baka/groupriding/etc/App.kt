package kr.baka.groupriding.etc

import android.app.Application
import android.content.Context
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

        var location = MutableLiveData<Pair<Int,Int>>()

        var sumOfSpeed = 0
        var countOfSampling = 0
    }


    override fun onCreate() {
        context = applicationContext
        sharedPreferences = MySharedPreferences(context)
        themeColor.value = sharedPreferences.themeColor
        themeColor.observeForever {
            sharedPreferences.themeColor = it
        }
        super.onCreate()
    }

}