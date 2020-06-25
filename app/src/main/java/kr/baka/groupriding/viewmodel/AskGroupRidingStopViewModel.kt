package kr.baka.groupriding.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.SingleLiveData

class AskGroupRidingStopViewModel : ViewModel(){
    var eventStopGroupRidingService = SingleLiveData<Any>()
    var eventCloseDialog = SingleLiveData<Any>()

    fun stopGroupRidingService(){
        eventStopGroupRidingService.call()
    }

    fun closeDialog(){
        eventCloseDialog.call()
    }
}