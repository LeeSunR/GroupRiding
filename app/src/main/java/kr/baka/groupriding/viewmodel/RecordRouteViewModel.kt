package kr.baka.groupriding.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.SingleLiveData

class RecordRouteViewModel : ViewModel(){
    var eventCloseDialog = SingleLiveData<Any>()

    fun closeDialog(){
        eventCloseDialog.call()
    }

    var startRecordRouteServiceEvent = SingleLiveData<Any>()
    fun startRecordRouteService(){
        startRecordRouteServiceEvent.call()
    }
}