package kr.baka.groupriding.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.SingleLiveData
import kr.baka.groupriding.repository.SettingRepository

class SettingViewModel :ViewModel(){

    val tag = SettingViewModel::class.java.simpleName

    //event
    var eventShowNicknameChangeDialog = SingleLiveData<Any>()
    var eventShowMaxSpeedChangeDialog = SingleLiveData<Any>()
    var eventShowSamplingIntervalChangeDialog = SingleLiveData<Any>()

    val nickname:MutableLiveData<String> by lazy { MutableLiveData<String>().also { it.value=SettingRepository.nickname } }
    val maxSpeed:MutableLiveData<String> by lazy { MutableLiveData<String>().also { it.value=SettingRepository.maxSpeed.toString() } }
    val samplingInterval:MutableLiveData<String> by lazy { MutableLiveData<String>().also { it.value=SettingRepository.samplingInterval.toString() } }


    fun nicknameChange(newNickname: String){
        SettingRepository.nickname = newNickname
        nickname.value = SettingRepository.nickname
    }

    fun maxSpeedChange(newMaxSpeed: Int){
        SettingRepository.maxSpeed = newMaxSpeed
        maxSpeed.value = SettingRepository.maxSpeed.toString()
    }

    fun samplingIntervalChange(newSamplingInterval: Int){
        SettingRepository.samplingInterval = newSamplingInterval
        samplingInterval.value = SettingRepository.samplingInterval.toString()
    }

    fun showNicknameChangeDialog(){
        eventShowNicknameChangeDialog.call()
    }

    fun showMaxSpeedChangeDialog(){
        eventShowMaxSpeedChangeDialog.call()
    }

    fun showSamplingIntervalChangeDialog(){
        eventShowSamplingIntervalChangeDialog.call()
    }
}