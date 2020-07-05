package kr.baka.groupriding.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.SingleLiveData

class RecordRouteSaveViewModel : ViewModel(){
    var eventCloseDialog = SingleLiveData<Any>()
    var recordSaveEvent = SingleLiveData<Any>()
    var name = MutableLiveData<String>()

    fun closeDialog(){
        eventCloseDialog.call()
    }

    fun recordSave(){
        recordSaveEvent.call()
    }
}