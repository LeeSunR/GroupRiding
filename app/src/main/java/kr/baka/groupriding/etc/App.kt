package kr.baka.groupriding.etc

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData

class App:Application(){

    companion object{
        lateinit var context: Context
        var speedLiveData = MutableLiveData<String>()
        var distanceLiveData = MutableLiveData<String>()
        var avgSpeedLiveData = MutableLiveData<String>()
        var maxSpeedLiveData = MutableLiveData<String>()
        var ridingTimeLiveData = MutableLiveData<String>()
        var restTimeLiveData = MutableLiveData<String>()
        var sumOfSpeed = Int
        var countOfSampling = Int
    }


    override fun onCreate() {
        context = applicationContext
        super.onCreate()
    }

}