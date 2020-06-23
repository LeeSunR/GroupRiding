package kr.baka.groupriding.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.SingleLiveData

class AskGroupRidingStartViewModel : ViewModel(){
    var eventStartGroupRidingService = SingleLiveData<Any>()

    fun startGroupRidingService(){
        eventStartGroupRidingService.call()
    }
}