package kr.baka.groupriding.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.SingleLiveData
import kr.baka.groupriding.repository.SettingRepository

class GroupRidingInviteViewModel : ViewModel(){
    var eventCloseDialog = SingleLiveData<Any>()
    val inviteCode:MutableLiveData<String> by lazy { SettingRepository.inviteCode }


    fun closeDialog(){
        eventCloseDialog.call()
    }
}