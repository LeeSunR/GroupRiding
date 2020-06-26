package kr.baka.groupriding.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.SingleLiveData

class GroupCodeShowViewModel : ViewModel(){
    var eventCloseDialog = SingleLiveData<Any>()
    var inviteCode = MutableLiveData<String>()

    init {
        App.inviteCode.observeForever {
            inviteCode.value = it
        }
    }

    fun closeDialog(){
        eventCloseDialog.call()
    }
}