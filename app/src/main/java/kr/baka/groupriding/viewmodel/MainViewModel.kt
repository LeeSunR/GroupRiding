package kr.baka.groupriding.viewmodel

import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.SingleLiveData
import kr.baka.groupriding.model.Information

@Parcelize
class MainViewModel: ViewModel(), Parcelable {

    val tag = MainViewModel::class.java.simpleName



    val speed = MutableLiveData<String>()
    val groupRidingStartVisibility = MutableLiveData<Int>()
    val groupRidingStopVisibility = MutableLiveData<Int>()

    init {
        observeForever()
    }






    private fun observeForever(){
        App.isGroupRidingServiceRunning.observeForever {
            if (it){
                groupRidingStopVisibility.value = View.GONE
                groupRidingStartVisibility.value = View.VISIBLE
            }
            else {
                groupRidingStopVisibility.value = View.VISIBLE
                groupRidingStartVisibility.value = View.GONE
            }
        }
    }
}