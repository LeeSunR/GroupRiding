package kr.baka.groupriding.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.SingleLiveData

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

    val groupRidingStartVisibility = MutableLiveData<Int>()
    val groupRidingStopVisibility = MutableLiveData<Int>()
    private fun observeForever(){
        App.isGroupRidingServiceRunning.observeForever {
            if (it){
                groupRidingStopVisibility.value = View.GONE
                groupRidingStartVisibility.value = View.VISIBLE
            }
            else {
                groupRidingStopVisibility.value = View.VISIBLE
                groupRidingStartVisibility.value = View.GONE
            }
        }
    }

    init {
        observeForever()
    }

}