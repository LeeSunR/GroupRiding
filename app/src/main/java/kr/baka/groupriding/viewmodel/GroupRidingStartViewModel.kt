package kr.baka.groupriding.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.SingleLiveData

class GroupRidingStartViewModel : ViewModel(){
    var eventCreateGroupRidingService = SingleLiveData<Any>()
    var eventJoinGroupDialogShow = SingleLiveData<Any>()

    fun createGroupRidingService(){
        eventCreateGroupRidingService.call()
    }

    fun joinGroupDialogShow(){
        eventJoinGroupDialogShow.call()
    }
}