package kr.baka.groupriding.viewmodel

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.baka.groupriding.R
import kr.baka.groupriding.etc.App
import kr.baka.groupriding.etc.SingleLiveData

class SimpleDialogViewModel : ViewModel(){

    val leftOnClick = MutableLiveData<View.OnClickListener>()
    val rightOnClick = MutableLiveData<View.OnClickListener>()

    val leftBtnText = MutableLiveData<String>()
    val rightBtnText = MutableLiveData<String>()

    val title = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    val bigMessageVisibility = MutableLiveData<Int>().also { it.value = View.GONE }
    val bigMessage = MutableLiveData<String>()
    val editTextVisibility = MutableLiveData<Int>().also { it.value = View.GONE }
    val editText = MutableLiveData<String>()
    val editTextHint = MutableLiveData<String>()

    val btnColor = MutableLiveData<Int>().also { it.value = App.context.getColor(R.color.colorButtonConfirm) }
}