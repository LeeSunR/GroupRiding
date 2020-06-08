package kr.baka.groupriding.etc

import android.app.Application
import androidx.lifecycle.MutableLiveData

object App:Application(){


    var speedLiveData = MutableLiveData<String>()

    override fun onCreate() {
        super.onCreate()
    }

}