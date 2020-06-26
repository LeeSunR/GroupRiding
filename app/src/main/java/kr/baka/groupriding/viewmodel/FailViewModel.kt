package kr.baka.groupriding.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.SingleLiveData

class FailViewModel : ViewModel(){
    var eventCloseDialog = SingleLiveData<Any>()
    var title = MutableLiveData<String>()
    var message = MutableLiveData<String>()

    fun closeDialog(){
        eventCloseDialog.call()
    }
}