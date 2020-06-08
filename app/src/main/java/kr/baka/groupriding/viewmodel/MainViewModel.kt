package kr.baka.groupriding.viewmodel

import android.content.Intent
import android.database.Observable
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.TextView
import android.widget.ToggleButton
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.App.speedLiveData
import kr.baka.groupriding.model.Information
import kr.baka.groupriding.service.RidingService
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KClass

@Parcelize
class MainViewModel: ViewModel(), Parcelable {
    val layoutTopLeft = MutableLiveData<Information>()
    val layoutTopRight = MutableLiveData<Information>()
    val layoutMiddle = MutableLiveData<Information>()
    val layoutBottomLeft = MutableLiveData<Information>()
    val layoutBottomRight = MutableLiveData<Information>()
    var tgRidingStatus = MutableLiveData<Boolean>()


    init {
        layoutTopLeft.value = Information(Information.INFO_TYPE_SUB_FLOAT,"타이틀",ObservableField<String>())
        layoutTopRight.value = Information(Information.INFO_TYPE_SUB_FLOAT,"타이틀",ObservableField<String>())
        layoutMiddle.value = Information(Information.INFO_TYPE_MAIN_FLOAT,"타이틀",ObservableField<String>())
        layoutBottomLeft.value = Information(Information.INFO_TYPE_SUB_TIME,"타이틀",ObservableField<String>())
        layoutBottomRight.value = Information(Information.INFO_TYPE_SUB_TIME,"타이틀",ObservableField<String>())
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun click(){

    }

    fun startRuning(boolean: Boolean){
        layoutMiddle.value!!.value.set("-1")

        speedLiveData.observeForever {
            layoutMiddle.value!!.value.set(it)
        }
        tgRidingStatus.value = boolean
    }

}