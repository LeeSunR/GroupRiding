package kr.baka.groupriding.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.SingleLiveData

class GroupRidingJoinViewModel : ViewModel(){
    var eventCloseDialog = SingleLiveData<Any>()
    var eventJoinGroupRidingService = SingleLiveData<Any>()
    var inviteCode = ObservableField<String>()

    fun closeDialog(){
        eventCloseDialog.call()
    }

    fun joinGroupRidingService(){
        eventJoinGroupRidingService.call()
    }
}