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
import kr.baka.groupriding.etc.SingleLiveData
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

    val startSettingActivityEvent = SingleLiveData<Any>()

    init {
        layoutTopLeft.value = Information(Information.TYPE_DISTANCE,Information.SIZE_SUB_FLOAT)
        layoutTopRight.value = Information(Information.TYPE_ACG_SPEED,Information.SIZE_SUB_FLOAT)
        layoutMiddle.value = Information(Information.TYPE_SPEED,Information.SIZE_MAIN_FLOAT)
        layoutBottomLeft.value = Information(Information.TYPE_RIDING_TIME,Information.SIZE_SUB_TIME)
        layoutBottomRight.value = Information(Information.TYPE_REST_TIME,Information.SIZE_SUB_TIME)
        observeForever()
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun click(){

    }

    fun startRuning(boolean: Boolean){
        tgRidingStatus.value = boolean
    }

    fun startSettingActivity(){
        startSettingActivityEvent.call()
    }

    private fun observeForever(){
        observeLayout(layoutTopLeft)
        observeLayout(layoutTopRight)
        observeLayout(layoutMiddle)
        observeLayout(layoutBottomLeft)
        observeLayout(layoutBottomRight)
    }

    private fun observeLayout(layout:MutableLiveData<Information>){
        when(layout.value!!.getType()){
            Information.TYPE_DISTANCE->
                App.distanceLiveData.observeForever{
                    layout.value!!.setData(it)
                }
            Information.TYPE_ACG_SPEED->
                App.avgSpeedLiveData.observeForever{
                    layout.value!!.setData(it)
                }
            Information.TYPE_SPEED->
                App.speedLiveData.observeForever{
                    layout.value!!.setData(it)
                }
            Information.TYPE_RIDING_TIME->
                App.ridingTimeLiveData.observeForever{
                    layout.value!!.setData(it)
                }
            Information.TYPE_REST_TIME->
                App.restTimeLiveData.observeForever{
                    layout.value!!.setData(it)
                }
        }
    }
}