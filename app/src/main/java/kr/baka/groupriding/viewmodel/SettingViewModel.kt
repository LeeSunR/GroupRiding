package kr.baka.groupriding.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.SingleLiveData
import kr.baka.groupriding.repository.SettingRepository

class SettingViewModel :ViewModel(){

    val tag = SettingViewModel::class.java.simpleName

    //repository
    private val myInfoRepository:SettingRepository by lazy { SettingRepository() }

    //event
    var eventShowNicknameChangeDialog = SingleLiveData<Any>()
    var eventShowMaxSpeedChangeDialog = SingleLiveData<Any>()
    var eventShowSamplingIntervalChangeDialog = SingleLiveData<Any>()

    val nickname:MutableLiveData<String> by lazy { MutableLiveData<String>().also { it.value=myInfoRepository.nickname } }
    val maxSpeed:MutableLiveData<String> by lazy { MutableLiveData<String>().also { it.value=myInfoRepository.maxSpeed.toString() } }
    val samplingInterval:MutableLiveData<String> by lazy { MutableLiveData<String>().also { it.value=myInfoRepository.samplingInterval.toString() } }


    fun nicknameChange(newNickname: String){
        myInfoRepository.nickname = newNickname
        nickname.value = myInfoRepository.nickname
    }

    fun maxSpeedChange(newMaxSpeed: Int){
        myInfoRepository.maxSpeed = newMaxSpeed
        maxSpeed.value = myInfoRepository.maxSpeed.toString()
    }

    fun samplingIntervalChange(newSamplingInterval: Int){
        myInfoRepository.samplingInterval = newSamplingInterval
        samplingInterval.value = myInfoRepository.samplingInterval.toString()
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