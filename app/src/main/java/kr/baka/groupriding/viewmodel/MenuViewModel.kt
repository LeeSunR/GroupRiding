package kr.baka.groupriding.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.SingleLiveData
import kr.baka.groupriding.repository.ServiceStatusLiveData

class MenuViewModel :ViewModel(){

    var startGroupRidingServiceEvent = SingleLiveData<Any>()
    fun startGroupRidingService(){
        startGroupRidingServiceEvent.call()
    }

    var stopGroupRidingServiceEvent = SingleLiveData<Any>()
    fun stopGroupRidingService(){
        stopGroupRidingServiceEvent.call()
    }

    var showSettingActivityEvent = SingleLiveData<Any>()
    fun showSettingActivity(){
        showSettingActivityEvent.call()
    }

    var inviteCodeDialogShowEvent = SingleLiveData<Any>()
    fun inviteCodeDialogShow(){
        inviteCodeDialogShowEvent.call()
    }

    var showRouteActivityEvent = SingleLiveData<Any>()
    fun showRouteActivity(){
        showRouteActivityEvent.call()
    }

    var showRecordRouteDialogEvent = SingleLiveData<Any>()
    fun showRecordRouteDialog(){
        showRecordRouteDialogEvent.call()
    }

    var showRecordRouteStopDialogEvent = SingleLiveData<Any>()
    fun showRecordRouteStopDialog(){
        showRecordRouteStopDialogEvent.call()
    }

    val groupRidingStartVisibility = MutableLiveData<Int>()
    val groupRidingStopVisibility = MutableLiveData<Int>()

    val recordRouteStartVisibility = MutableLiveData<Int>()
    val recordRouteStopVisibility = MutableLiveData<Int>()

    init {
        observeForever()
    }

    private fun observeForever(){
        ServiceStatusLiveData.groupingService.observeForever {
            if (it){
                groupRidingStopVisibility.value = View.GONE
                groupRidingStartVisibility.value = View.VISIBLE
            }
            else {
                groupRidingStopVisibility.value = View.VISIBLE
                groupRidingStartVisibility.value = View.GONE
            }
        }

        ServiceStatusLiveData.recordingService.observeForever {
            if (it){
                recordRouteStopVisibility.value = View.GONE
                recordRouteStartVisibility.value = View.VISIBLE
            }
            else {
                recordRouteStopVisibility.value = View.VISIBLE
                recordRouteStartVisibility.value = View.GONE
            }
        }
    }
}