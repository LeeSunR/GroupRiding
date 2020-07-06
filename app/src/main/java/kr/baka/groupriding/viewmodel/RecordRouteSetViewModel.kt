package kr.baka.groupriding.viewmodel

import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.SingleLiveData

class RecordRouteSetViewModel : ViewModel(){
    var eventCloseDialog = SingleLiveData<Any>()

    fun closeDialog(){
        eventCloseDialog.call()
    }

    var stopRecordRouteServiceEvent = SingleLiveData<Any>()
    fun stopRecordRouteService(){
        stopRecordRouteServiceEvent.call()
    }
}