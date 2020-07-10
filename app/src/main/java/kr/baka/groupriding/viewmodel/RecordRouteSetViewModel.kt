package kr.baka.groupriding.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.etc.SingleLiveData

class RecordRouteSetViewModel : ViewModel(){
    val leftOnClick = MutableLiveData<View.OnClickListener>()
    val rightOnClick = MutableLiveData<View.OnClickListener>()
}